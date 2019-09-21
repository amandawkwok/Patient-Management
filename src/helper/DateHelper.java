package helper;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateHelper {

	// Converts a String in "MM/dd/yyy" format to an SQL Date
	public static Date convertToSQLDate(String date) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
			java.util.Date formattedDate = dateFormatter.parse(date);
			java.sql.Date birthday = new java.sql.Date(formattedDate.getTime());
			return birthday;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isValidDate(String dateString) {
		boolean isDate = false;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		format.setLenient(false);
		try {
			if (dateString.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
				format.parse(dateString);
				isDate = true;
			}
		} catch (ParseException pe) {
			isDate = false;
		}
		return isDate;
	}
}
