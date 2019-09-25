package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Appointment;
import entity.Patient;

@WebServlet("/ViewPatients")
public class ViewPatients extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Retrieves all patients for view_patients.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filterClause = request.getParameter("filterClause");
		ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();

		if (filterClause == null || filterClause.trim().length() == 0) {
			request.setAttribute("searchHeader", "Showing all patients");
			aList = Patient.getByFilter("");
		} else {
			String[] searchKeyArray = filterClause.split("\\s+");
			if (searchKeyArray.length == 1) {
				request.setAttribute("searchHeader", "Showing results for: " + filterClause);
				aList = Patient.getByFilter(" WHERE UPPER(FirstName) LIKE UPPER(\"" + searchKeyArray[0]
						+ "%\") OR UPPER(LastName) LIKE UPPER(\"" + searchKeyArray[0] + "%\")");
			} else if (searchKeyArray.length == 2) {
				request.setAttribute("searchHeader", "Showing results for: " + filterClause);
				aList = Patient.getByFilter(" WHERE UPPER(FirstName) LIKE UPPER(\"" + searchKeyArray[0]
						+ "\") AND UPPER(LastName) LIKE UPPER(\"" + searchKeyArray[1] + "%\")");
			} else if (searchKeyArray.length > 2) {
				request.setAttribute("searchHeader", "No results found for: " + filterClause);
			}
		}

		request.setAttribute("filterClause", filterClause);
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

		Map<String, String> patientInformation = Patient.getAttributeValuePairsBySSN(primaryKeyNumeric);

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
}