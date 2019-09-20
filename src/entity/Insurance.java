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

public class Insurance {

	public static void main(String[] args) throws ClassNotFoundException, ParseException {
		// add(888, "name", "address", "id", "group", 0,
		// update(888, "InsuranceName", "InsuranceAddress", "InsuranceID",
		// "InsuranceGroup", 36l,
		// DateHelper.convertToSQLDate("09/15/2008"), "InsuranceHolderName",
		// 37l,
		// DateHelper.convertToSQLDate("10/22/2020"));
		System.out.println(getBySSN(888));
	}

	public static void add(long ssn, String name, String address, String id, String group, long copay,
			Date effectiveDate, String policyHolderName, long policyHolderSSN, Date policyHolderBirthday)
			throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement("INSERT INTO INSURANCE VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, ssn);
			stmt.setString(2, name);
			stmt.setString(3, address);
			stmt.setString(4, id);
			stmt.setString(5, group);
			stmt.setLong(6, copay);
			stmt.setDate(7, effectiveDate);
			stmt.setString(8, policyHolderName);
			stmt.setLong(9, policyHolderSSN);
			stmt.setDate(10, policyHolderBirthday);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static List<String> getBySSN(long ssn) {
		List<String> insurance = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"SELECT ssn, name, address, id, groupNum, copay, DATE_FORMAT(effectiveDate, \"%m/%d/%Y\"), "
							+ "policyHolderName, policyHolderSSN, DATE_FORMAT(policyHolderBirthday, \"%m/%d/%Y\") FROM "
							+ "Insurance WHERE ssn = ?");
			stmt.setLong(1, ssn);

			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsMeta = rs.getMetaData();
			int columnCount = rsMeta.getColumnCount();

			while (rs.next()) {
				for (int col = 1; col <= columnCount; col++) {
					insurance.add(rs.getString(col));
				}
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return insurance;
	}

	public static void update(long ssn, String name, String address, String id, String group, long copay,
			Date effectiveDate, String policyHolderName, long policyHolderSSN, Date policyHolderBirthday)
			throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn
					.prepareStatement("UPDATE Insurance SET name = ?, address = ?, id = ? , groupNum = ? , copay = ? , "
							+ "effectiveDate = ?, policyHolderName = ?, policyHolderSSN = ?, policyHolderBirthday = ? "
							+ "WHERE ssn = ?");
			stmt.setString(1, name);
			stmt.setString(2, address);
			stmt.setString(3, id);
			stmt.setString(4, group);
			stmt.setLong(5, copay);
			stmt.setDate(6, effectiveDate);
			stmt.setString(7, policyHolderName);
			stmt.setLong(8, policyHolderSSN);
			stmt.setDate(9, policyHolderBirthday);
			stmt.setLong(10, ssn);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}
