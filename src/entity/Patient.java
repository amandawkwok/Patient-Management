package entity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import helper.ConnectionCredentials;

public class Patient {

	public static void main(String[] args) throws ClassNotFoundException, ParseException {
		// add(33333, "First3", "Middle3", "Last3", birthday, false);
		// update(1234567890, 888, "NewAmanda", "NewW", "NewKwok",
		System.out.println(getBySSN(888));
	}

	public static void add(long ssn, String first, String middle, String last, Date birthday, boolean sex)
			throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("INSERT INTO Patient VALUES (?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, ssn);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, birthday);
			stmt.setBoolean(6, sex);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static List<String> getBySSN(long ssn) {
		List<String> patient = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"SELECT ssn, first, middle, last, DATE_FORMAT(dob, \"%m/%d/%Y\"), sex FROM Patient WHERE ssn = ?");
			stmt.setLong(1, ssn);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					patient.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return patient;
	}

	public static void delete(long ssn) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("DELETE FROM Patient WHERE ssn = ?");
			stmt.setLong(1, ssn);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static boolean exists(long ssn) {
		boolean exists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("SELECT * FROM Patient WHERE ssn = ?");
			stmt.setLong(1, ssn);
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

	public static void update(long oldSSN, long newSSN, String first, String middle, String last, Date birthday,
			boolean sex) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement(
					"UPDATE Patient SET ssn = ?, first = ?, middle = ?, last = ?, dob = ?, sex = ? WHERE ssn = ?");
			stmt.setLong(1, newSSN);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, birthday);
			stmt.setBoolean(6, sex);
			stmt.setLong(7, oldSSN);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}
