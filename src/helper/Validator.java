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

	public static boolean isNumeric(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
