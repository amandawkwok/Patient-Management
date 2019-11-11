package helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeHelper {

	// Converts a String in "HH:mm" format to an SQL Time
	public static Timestamp convertToSQLDateTime(String dayTime) {
		try {
			SimpleDateFormat timeFormatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			java.util.Date formattedTime = timeFormatter.parse(dayTime);
			java.sql.Timestamp apptdayTime = 
					new java.sql.Timestamp(formattedTime.getTime());
			return apptdayTime;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isValidTime(String timeString) {
		boolean isTime = false;
		SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
		format.setLenient(false);
		try {
			if (timeString.matches("((1[0-2]|0?[1-9]):([0-5][0-9]) ?([AaPp][Mm]))")) {
				format.parse(timeString);
				isTime = true;
			}
		} catch (ParseException pe) {
			isTime = false;
		}
		return isTime;
	}
	
}
