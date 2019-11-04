package helper;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimeHelper {

	// Converts a String in "HH:mm" format to an SQL Time
	public static Time convertToSQLTime(String time) {
		try {
			SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm a");
			java.util.Date formattedTime = timeFormatter.parse(time);
			java.sql.Time apptTime = new java.sql.Time(formattedTime.getTime());
			return apptTime;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isValidTime(String timeString) {
		boolean isTime = false;
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
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
