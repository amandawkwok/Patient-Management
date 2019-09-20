package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

import helper.ConnectionCredentials;

public class Appointment {

	public static ArrayList<ArrayList<String>> getBySSNAndTimePeriod(long ssn, String timePeriod) {
		ArrayList<ArrayList<String>> totalAppointments = new ArrayList<ArrayList<String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			String query = "SELECT DATE_FORMAT(dayTime, \"%m/%d/%y %h:%i %p\"), reason, id FROM Appointment WHERE ssn = ? AND ";

			if (timePeriod.equals("upcoming")) {
				query += "dayTime >= CURRENT_TIMESTAMP ORDER BY dayTime ASC";
			} else {
				query += "dayTime < CURRENT_TIMESTAMP ORDER BY dayTime DESC";
			}

			stmt = conn.prepareStatement(query);
			stmt.setLong(1, ssn);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			ArrayList<String> subList = new ArrayList<String>();
			while (rs.next()) {
				subList = new ArrayList<String>();
				for (int col = 1; col <= columnCount; col++) {
					if (col == columnCount) {
						if (timePeriod.equals("upcoming")) {
							subList.add(
									"<form class=\"form-inline\"><input type=\"hidden\" name=\"appointmentID\" value=\""
											+ rs.getString(col)
											+ "\" /><button type=\"submit\" class=\"btn btn-primary\">Edit</button></form>");
						}
						subList.add("<form class=\"form-inline\"><input type=\"hidden\" name=\"appointmentID\" value=\""
								+ rs.getString(col)
								+ "\" /><button type=\"submit\" class=\"btn btn-danger\">Delete</button></form>");
					} else {
						subList.add(rs.getString(col));
					}
				}
				totalAppointments.add(subList);
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
			return new ArrayList<ArrayList<String>>();
		}
		return totalAppointments;
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}