package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import helper.ConnectionCredentials;

public class EmergencyContact {

	public static void main(String[] args) throws ClassNotFoundException, ParseException {
		// add(888, "Sandy", "Mother", 6265335204l);
		// update(888, "Frank", "Father", 5204l);
		System.out.println(getBySSN(888));
	}

	public static void add(long ssn, String name, String relationship, long number) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("INSERT INTO Emergency_Contact VALUES (?, ?, ?, ?)");
			stmt.setLong(1, ssn);
			stmt.setString(2, name);
			stmt.setString(3, relationship);
			stmt.setLong(4, number);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static List<String> getBySSN(long ssn) {
		List<String> emergencyContact = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement("SELECT * FROM Emergency_Contact WHERE ssn = ?");
			stmt.setLong(1, ssn);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					emergencyContact.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return emergencyContact;
	}

	public static void update(long ssn, String name, String relationship, long number) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement(
					"UPDATE Emergency_Contact SET name = ?, relationship = ?, number = ? WHERE ssn = ?");
			stmt.setString(1, name);
			stmt.setString(2, relationship);
			stmt.setLong(3, number);
			stmt.setLong(4, ssn);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}
