package entity;

import java.sql.Connection;
//import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
//import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import helper.ConnectionCredentials;

public class Appointment {
	
	
	public static void add(long ssn, Timestamp dayTime, String status, 
			String reason) throws ClassNotFoundException, SQLException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"INSERT INTO Appointment "
					+ "(Appointment.ssn, Appointment.dayTime, " 
					+ "Appointment.status, Appointment.reason) "
					+ "VALUES (?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);
			
			stmt.setLong(1, ssn);
			stmt.setTimestamp(2, dayTime);
			stmt.setString(3, status);
			stmt.setString(4, reason);
			
			int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating appointment failed, no rows affected.");
	        }
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void delete(int id) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("DELETE FROM Appointment WHERE id = ?");
			stmt.setInt(1, id);;
			stmt.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static boolean exists(int id) {
		boolean exists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("SELECT * FROM Appointment WHERE id = ?");
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				exists = true;
			}

			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return exists;
	}	
	
	public static List<String> getById(int id) {
		List<String> appointment = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"SELECT ssn, id, DATE_FORMAT(dayTime, \"%m/%d/%Y %h:%i %p\"), " 
							+ "status, reason "
							+ "FROM Appointment WHERE id = ?");
			stmt.setInt(1, id);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					appointment.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return appointment;
	}
	
	public static List<String> getAttributeNames() {
		List<String> attributes = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement("SELECT * FROM Appointment");
			
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
				attributes.add(rsMeta.getColumnName(i));
			}
			rs.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return attributes;
	}
	
	/**
	 * Returns a key-value pairing of Appointment attributes with their respective
	 * database values.
	 */
	public static Map<String, String> getAttributeValuePairsById(int id) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		
		List<String> attributes = getAttributeNames();
		List<String> values = new ArrayList<String>();
		
		// Retrieving Appointment Information
		values = Appointment.getById(id);
		int listSize = values.size();
		
		for (int i = 0; i < listSize; i++) {
			map.put(attributes.get(i), values.get(i));
		}
		
		return map;
	}
	
	public static ArrayList<ArrayList<String>> getBySSNAndTimePeriod(long ssn, String timePeriod) {
		ArrayList<ArrayList<String>> totalAppointments = new ArrayList<ArrayList<String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			String query = "SELECT DATE_FORMAT(dayTime, \"%m/%d/%Y %h:%i %p\"), reason, id FROM Appointment WHERE ssn = ? AND ";

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
						subList.add("<form class=\"form-inline\" method=\"post\" action=\"ModifyAppointment\"> "
								+ "<input type=\"hidden\" name=\"pageHeader\" value=\"Edit\"> "
								+ "<input type=\"hidden\" name=\"appointmentID\" value=\""
								+ rs.getString(col)
								+ "\" /><button type=\"submit\" class=\"btn btn-primary\">Edit</button></form>");
						subList.add("<form class=\"form-inline\" method=\"get\" action=\"DeleteAppointment\"> " 
								+ "<input type=\"hidden\" name=\"appointmentID\" value=\""
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
			System.out.println("error: " + e);
			return new ArrayList<ArrayList<String>>();
		}
		return totalAppointments;
	}

	public static List<String> getSSNFromId(int id) {
		List<String> patientSSN = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement("SELECT P.SSN FROM Patient P INNER JOIN " 
									   + "Appointment A ON P.SSN = A.SSN WHERE "
									   + "A.ID = ?");
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();
			
			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					patientSSN.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return patientSSN;
	}
	
	public static void update(int id, Timestamp dayTime, String status, String reason) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
				ConnectionCredentials.PASSWORD);
			stmt = conn
					.prepareStatement("UPDATE Appointment SET dayTime = ?, status = ?, reason = ? "
									+ "WHERE id = ?", Statement.RETURN_GENERATED_KEYS);
			
			stmt.setTimestamp(1, dayTime);
			stmt.setString(2, status);
			stmt.setString(3, reason);
			stmt.setInt(4, id);
			
			int affectedRows = stmt.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Creating user failed, no rows affected.");
	        }

			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}