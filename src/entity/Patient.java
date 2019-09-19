package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import enums.ConnectionCredentials;

public class Patient {

	public static boolean exists(long ssn) {
		boolean exists = false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Patient WHERE ssn = " + ssn);

			if (rs.next()) {
				exists = true;
			}

			rs.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return exists;
	}

	private static Connection conn;
	private static Statement stmt;
}
