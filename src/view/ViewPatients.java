package view;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Appointment;
import helper.ConnectionCredentials;
import helper.PatientFormHelper;

@WebServlet("/ViewPatients")
public class ViewPatients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Retrieves all patients for view_patients.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String searchKey = request.getParameter("searchKey");
		ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();

		if (searchKey == null || searchKey.trim().length() == 0) {
			request.setAttribute("searchHeader", "Showing all patients");
			aList = _getPatientsByFilter("");
		} else {
			String[] searchKeyArray = searchKey.split("\\s+");
			if (searchKeyArray.length == 1) {
				request.setAttribute("searchHeader", "Showing results for: " + searchKey);
				aList = _getPatientsByFilter(" WHERE UPPER(FirstName) LIKE UPPER(\"" + searchKeyArray[0]
						+ "%\") OR UPPER(LastName) LIKE UPPER(\"" + searchKeyArray[0] + "%\")");
			} else if (searchKeyArray.length == 2) {
				request.setAttribute("searchHeader", "Showing results for: " + searchKey);
				aList = _getPatientsByFilter(" WHERE UPPER(FirstName) LIKE UPPER(\"" + searchKeyArray[0]
						+ "\") AND UPPER(LastName) LIKE UPPER(\"" + searchKeyArray[1] + "%\")");
			} else if (searchKeyArray.length > 2) {
				request.setAttribute("searchHeader", "No results found for: " + searchKey);
			}
		}

		request.setAttribute("arrayList", aList);
		request.getRequestDispatcher("view_patients.jsp").include(request, response);
	}

	/**
	 * Retrieves an individual patient's information to be viewed in
	 * view_patient.jsp
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String primaryKey = request.getParameter("primaryKey");
		long primaryKeyNumeric = Long.parseLong(primaryKey);

		Map<String, String> patientInformation = PatientFormHelper.getFormFieldDatabasePairsBySSN(primaryKeyNumeric);

		for (Map.Entry<String, String> field : patientInformation.entrySet()) {
			request.setAttribute(field.getKey(), field.getValue());
		}

		ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(primaryKeyNumeric, "upcoming");
		ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(primaryKeyNumeric, "past");

		request.setAttribute("upcomingAppts", upcomingAppts);
		request.setAttribute("pastAppts", pastAppts);

		request.setAttribute("primaryKey", primaryKey);

		request.getRequestDispatcher("view_patient.jsp").include(request, response);
	}

	/**
	 * Executes a query to retrieve patient's first name, last name, next
	 * appointment, and phone number
	 * 
	 * @param filterClause
	 *            the filter condition to be met by the query
	 * @return a list of patient information
	 */
	private ArrayList<ArrayList<String>> _getPatientsByFilter(String filterClause) {
		System.out.println(filterClause);
		ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT FirstName, LastName, DATE_FORMAT(AppointmentDayTime, \"%m/%d/%y %h:%i %p\"), C.cell, AAAA.SSN "
							+ "FROM (SELECT P.SSN AS SSN, P.first as FirstName, P.last as LastName, AAA.dayTime as AppointmentDayTime "
							+ "FROM Patient P LEFT JOIN (SELECT * FROM jdbc.Appointment A WHERE A.dayTime>="
							+ "CURRENT_TIMESTAMP AND A.dayTime <= ALL (SELECT AA.dayTime FROM Appointment AA "
							+ "WHERE A.SSN = AA.SSN AND AA.dayTime>=CURRENT_TIMESTAMP)) AAA ON P.SSN=AAA.SSN)"
							+ " AAAA LEFT JOIN Contact C ON AAAA.SSN=C.SSN" + filterClause + " ORDER BY FirstName ASC");

			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount() - 1;

			while (rs.next()) {
				ArrayList<String> subList = new ArrayList<String>();

				for (int col = 1; col <= columnCount; col++) {
					String columnValue = rs.getString(col);

					if (columnValue == null) {
						subList.add("");
					} else {
						subList.add(rs.getString(col));
					}

					if (col == columnCount) {
						subList.add("<form method=\"post\" action=\"ViewPatients\"> "
								+ "<input type=\"hidden\" name=\"primaryKey\" value=\"" + rs.getString(col + 1) + "\">"
								+ "<input type=\"submit\" value=\"View\" class=\"btn btn-info\"></form>");
					}
				}
				aList.add(subList);
			}
			rs.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e);
		}

		return aList;
	}
}