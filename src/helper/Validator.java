package helper;

public class Validator {

	// Returns true if every character is white space, or null
	public static boolean isNull(String phrase) {
		if (phrase == null)
			return true;

		for (int i = 0; i < phrase.length(); i++)
			if (!Character.isWhitespace(phrase.charAt(i)))
				return false;

		return true;
	}

	//
	public static boolean isValidNumeric(String value) {
		boolean isValidNumeric = true;

		try {
			if (Long.parseLong(value) < 0) {
				isValidNumeric = false;
			}
		} catch (NumberFormatException e) {
			isValidNumeric = false;
		}

		return isValidNumeric;
	}
}
