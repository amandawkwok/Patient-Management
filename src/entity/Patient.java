package entity;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import helper.ConnectionCredentials;

public class Patient {

	public static void add(long ssn, String first, String middle, String last, Date dob, String sex, String address1,
			String address2, String city, String state, int zip, long cellPhone, long homePhone, long workPhone,
			String email, String emergencyName, String emergencyRelationship, long emergencyNumber,
			String insuranceName, String insuranceAddress, String insuranceId, String insuranceGroupNumber,
			long insuranceCopay, Date insuranceEffectiveDate, String policyHolderName, long policyHolderSSN,
			Date policyHolderBirthday) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn.prepareStatement(
					"INSERT INTO Patient VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
							+ "?, ?, ?, ?, ?, ?, ?, ?)");
			stmt.setLong(1, ssn);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, dob);
			stmt.setString(6, sex);
			stmt.setString(7, address1);
			stmt.setString(8, address2);
			stmt.setString(9, city);
			stmt.setString(10, state);
			stmt.setInt(11, zip);
			stmt.setLong(12, cellPhone);

			if (homePhone == java.sql.Types.BIGINT) {
				stmt.setNull(13, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(13, homePhone);
			}

			if (workPhone == java.sql.Types.BIGINT) {
				stmt.setNull(14, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(14, workPhone);
			}

			stmt.setString(15, email);
			stmt.setString(16, emergencyName);
			stmt.setString(17, emergencyRelationship);
			stmt.setLong(18, emergencyNumber);
			stmt.setString(19, insuranceName);
			stmt.setString(20, insuranceAddress);
			stmt.setString(21, insuranceId);
			stmt.setString(22, insuranceGroupNumber);

			if (insuranceCopay == java.sql.Types.BIGINT) {
				stmt.setNull(23, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(23, insuranceCopay);
			}

			stmt.setDate(24, insuranceEffectiveDate);
			stmt.setString(25, policyHolderName);

			if (policyHolderSSN == java.sql.Types.BIGINT) {
				stmt.setNull(26, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(26, policyHolderSSN);
			}

			stmt.setDate(27, policyHolderBirthday);

			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
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

	public static List<String> getBySSN(long ssn) {
		List<String> patient = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement(
					"SELECT ssn, first, middle, last, DATE_FORMAT(dob, \"%m/%d/%Y\"), sex, address1, address2, "
							+ "city, state, zip, cellPhone, homePhone, workPhone, email, emergencyName, emergencyRelationship, "
							+ "emergencyNumber, insuranceName, insuranceAddress, insuranceId, insuranceGroupNumber, insuranceCopay, "
							+ "DATE_FORMAT(insuranceEffectiveDate, \"%m/%d/%Y\"), policyHolderName, policyHolderSSN, DATE_FORMAT(policyHolderBirthday, \"%m/%d/%Y\") FROM Patient WHERE ssn = ?");
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

	public static List<String> getAttributeNames() {
		List<String> attributes = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);

			stmt = conn.prepareStatement("SELECT * FROM Patient");

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
	 * Returns a key-value pairing of Patient attributes with their respective
	 * database values
	 */
	public static Map<String, String> getAttributeValuePairsBySSN(long ssn) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		List<String> attributes = getAttributeNames();
		List<String> values = new ArrayList<String>();

		// Retrieving Patient information
		values = Patient.getBySSN(ssn);
		int listSize = values.size();

		for (int i = 0; i < listSize; i++) {
			map.put(attributes.get(i), values.get(i));
		}

		return map;
	}

	/**
	 * Executes a query to retrieve patient's first name, last name, next
	 * appointment, and phone number as well as a button to view the patient's
	 * profile
	 */
	public static ArrayList<ArrayList<String>> getByFilter(String filterClause) {

		ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			Statement stmt = conn.createStatement();

			ResultSet rs = stmt.executeQuery(
					"SELECT FirstName, LastName, DATE_FORMAT(AppointmentDayTime, \"%m/%d/%y %h:%i %p\"), CellPhone, AAAA.SSN "
							+ "FROM (SELECT P.SSN AS SSN, P.first as FirstName, P.last as LastName, AAA.dayTime as AppointmentDayTime, P.cellPhone as CellPhone "
							+ "FROM Patient P LEFT JOIN (SELECT * FROM jdbc.Appointment A WHERE A.dayTime>="
							+ "CURRENT_TIMESTAMP AND A.dayTime <= ALL (SELECT AA.dayTime FROM Appointment AA "
							+ "WHERE A.SSN = AA.SSN AND AA.dayTime>=CURRENT_TIMESTAMP)) AAA ON P.SSN=AAA.SSN)"
							+ " AAAA " + filterClause);

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
						//System.out.println(rs.getString(col + 1));
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

	public static void update(long oldSSN, long ssn, String first, String middle, String last, Date dob, String sex,
			String address1, String address2, String city, String state, int zip, long cellPhone, long homePhone,
			long workPhone, String email, String emergencyName, String emergencyRelationship, long emergencyNumber,
			String insuranceName, String insuranceAddress, String insuranceId, String insuranceGroupNumber,
			long insuranceCopay, Date insuranceEffectiveDate, String policyHolderName, long policyHolderSSN,
			Date policyHolderBirthday) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(ConnectionCredentials.URL, ConnectionCredentials.USERNAME,
					ConnectionCredentials.PASSWORD);
			stmt = conn
					.prepareStatement("UPDATE Patient SET ssn = ?, first = ?, middle = ?, last = ?, dob = ?, sex = ?, "
							+ "address1 = ?, address2 = ?, city = ?, state = ?, zip = ?, cellPhone = ?, homePhone = ?, "
							+ "workPhone = ?, email = ?, emergencyName = ?, emergencyRelationship = ?, emergencyNumber = ?, "
							+ "insuranceName = ?, insuranceAddress = ?, insuranceId = ?, insuranceGroupNumber = ?, insuranceCopay = ?, "
							+ "insuranceEffectiveDate = ?, policyHolderName = ?, policyHolderSSN = ?, policyHolderBirthday = ? "
							+ "WHERE ssn = ?");
			stmt.setLong(1, ssn);
			stmt.setString(2, first);
			stmt.setString(3, middle);
			stmt.setString(4, last);
			stmt.setDate(5, dob);
			stmt.setString(6, sex);
			stmt.setString(7, address1);
			stmt.setString(8, address2);
			stmt.setString(9, city);
			stmt.setString(10, state);
			stmt.setInt(11, zip);
			stmt.setLong(12, cellPhone);

			if (homePhone == java.sql.Types.BIGINT) {
				stmt.setNull(13, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(13, homePhone);
			}

			if (workPhone == java.sql.Types.BIGINT) {
				stmt.setNull(14, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(14, workPhone);
			}

			stmt.setString(15, email);
			stmt.setString(16, emergencyName);
			stmt.setString(17, emergencyRelationship);
			stmt.setLong(18, emergencyNumber);
			stmt.setString(19, insuranceName);
			stmt.setString(20, insuranceAddress);
			stmt.setString(21, insuranceId);
			stmt.setString(22, insuranceGroupNumber);

			if (insuranceCopay == java.sql.Types.BIGINT) {
				stmt.setNull(23, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(23, insuranceCopay);
			}

			stmt.setDate(24, insuranceEffectiveDate);
			stmt.setString(25, policyHolderName);

			if (policyHolderSSN == java.sql.Types.BIGINT) {
				stmt.setNull(26, java.sql.Types.BIGINT);
			} else {
				stmt.setLong(26, policyHolderSSN);
			}

			stmt.setDate(27, policyHolderBirthday);
			stmt.setLong(28, oldSSN);
			stmt.executeUpdate();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static Connection conn;
	private static PreparedStatement stmt;
}
