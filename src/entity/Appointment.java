package entity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import helper.ConnectionCredentials;

public class Appointment {

	public static void add(long id, String first, String middle, String last, Date date, Time dayTime,
							String status, String reason) throws ClassNotFoundException{
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement(
					"INSERT INTO Appointment VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			
			stmt.setLong(1, id);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, date);
			stmt.setTime(6, dayTime);
			stmt.setString(7, status);
			stmt.setString(8, reason);
			
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	public static void delete(long id) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("DELETE FROM Appointment WHERE id = ?");
			stmt.setLong(1,  id);;
			stmt.executeUpdate();
			conn.close();
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static boolean exists(long id) {
		boolean exists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("SELECT * FROM Appointment WHERE id = ?");
			stmt.setLong(1, id);
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
	
	public static List<String> getById(long id) {
		List<String> appointment = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"SELECT id, first, middle, last, DATE_FORMAT(date, \"%m/%d/%Y\"), " 
							+ "TIME_FORMAT(dayTime, \"%h:%i %p\"), status, reason, "
							+ "FROM Patient WHERE ssn = ?");
			stmt.setLong(1, id);

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
	public static Map<String, String> getAttributeValuePairsbyId(long id) {
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

			//String query = "SELECT TIME_FORMAT(dayTime, \"%h:%i %p\"), DATE_FORMAT(dob, \"%m/%d/%Y\"), reason, id FROM Appointment WHERE ssn = ? AND ";
			String query = "SELECT TIME_FORMAT(dayTime, \"%h:%i %p\"), reason, id FROM Appointment WHERE ssn = ? AND ";

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
						subList.add("<form class=\"form-inline\" method=\"post\" action=\"ViewPatients\"> " 
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

	public static void update(long oldId, long id, String first, String middle, String last, Date date, Time dayTime,
			String status, String reason) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
				ConnectionCredentials.PASSWORD);
			stmt = conn
					.prepareStatement("UPDATE Appointment SET id = ?, first = ?, middle = ?, "
									+ "last = ?, date = ?, dayTime = ?, status = ?, reason = ? "
									+ "WHERE id = ?");
			
			stmt.setLong(1, id);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, date);
			stmt.setTime(6, dayTime);
			stmt.setString(7, status);
			stmt.setString(8, reason);
			stmt.setLong(9, oldId);
			
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}

	}
	
	private static Connection conn;
	private static PreparedStatement stmt;
}