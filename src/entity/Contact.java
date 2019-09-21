package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import helper.ConnectionCredentials;

public class Contact {

	public static void add(long ssn, long cell, long home, long work, String email) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("INSERT INTO Contact VALUES (?, ?, ?, ?, ?)");
			stmt.setLong(1, ssn);
			stmt.setLong(2, cell);

			if (home == java.sql.Types.BIGINT) {
				stmt.setNull(3, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(3, home);
			}

			if (work == java.sql.Types.BIGINT) {
				stmt.setNull(4, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(4, work);
			}

			stmt.setString(5, email);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static List<String> getBySSN(long ssn) {
		List<String> contact = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement("SELECT * FROM Contact WHERE ssn = ?");
			stmt.setLong(1, ssn);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					contact.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return contact;
	}

	public static void update(long ssn, long cell, long home, long work, String email) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("UPDATE Contact SET cell = ?, home = ?, work = ?, email = ? WHERE ssn = ?");
			stmt.setLong(1, cell);

			if (home == java.sql.Types.BIGINT) {
				stmt.setNull(2, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(2, home);
			}

			if (work == java.sql.Types.BIGINT) {
				stmt.setNull(3, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(3, work);
			}

			stmt.setString(4, email);
			stmt.setLong(5, ssn);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}
