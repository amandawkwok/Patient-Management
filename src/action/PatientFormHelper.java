package action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import entity.Patient;

public class PatientFormHelper {
	public static List<String> validateInput(HttpServletRequest request) {
		LinkedHashMap<String, String> lhm = getFormFieldInputPairs(request);
		List<String> errors = new ArrayList<String>();

		// PATIENT
		if (isNull(lhm.get("firstName"))) {
			errors.add("First Name cannot be left blank.");
		}

		if (isNull(lhm.get("lastName"))) {
			errors.add("Last Name cannot be left blank.");
		}

		if (isNull(lhm.get("birthday")) || !isValidDate(lhm.get("birthday"))) {
			errors.add("Patient birthday is not valid.");
		}

		String ssn = lhm.get("ssn");

		if (isNull(ssn) || !isNumeric(ssn) || String.valueOf(ssn).length() != 10) {
			errors.add("Patient SSN must be a 10 digit number.");
		} else {
			Long ssnObject = Long.parseLong(ssn);
			long ssnPrimitive = ssnObject.longValue();
			if (Patient.exists(ssnPrimitive)) {
				errors.add("A patient with that SSN already exists.");
			}
		}

		// ADDRESS
		if (isNull(lhm.get("address1"))) {
			errors.add("Address 1 cannot be left blank.");
		}

		if (isNull(lhm.get("city"))) {
			errors.add("City cannot be left blank.");
		}

		if (isNull(lhm.get("zip")) || !isNumeric(lhm.get("zip"))) {
			errors.add("Zip code is not a valid number.");
		}

		// CONTACT
		if (isNull(lhm.get("cellPhone")) || !isNumeric(lhm.get("cellPhone"))) {
			errors.add("Cell phone number is not valid.");
		}

		if (!lhm.get("homePhone").equals("") && !isNumeric(lhm.get("homePhone"))) {
			errors.add("Home phone number is not valid.");
		}

		if (!lhm.get("workPhone").equals("") && !isNumeric(lhm.get("workPhone"))) {
			errors.add("Work phone number is not valid.");
		}

		// EMERGENCY CONTACT
		if (isNull(lhm.get("emergencyName"))) {
			errors.add("Emergency Contact Name cannot be left blank.");
		}

		if (lhm.get("emergencyNumber").equals("") || !isNumeric(lhm.get("emergencyNumber"))) {
			errors.add("Emergency Contact Number is not valid.");
		}

		// INSURANCE
		if (isNull(lhm.get("insuranceID"))) {
			errors.add("Insurance ID cannot be left blank.");
		}

		if (lhm.get("insuranceCopay").equals("") && !isNumeric(lhm.get("insuranceCopay"))) {
			errors.add("Insurance Copay is not a valid number.");
		}

		if (!isValidDate(lhm.get("effectiveDate"))) {
			errors.add("Effective Date must be in mm/dd/yyyy format.");
		}

		if (isNull(lhm.get("policyHolderName"))) {
			errors.add("Policy Holder Name cannot be left blank.");
		}

		String insuranceSsn = lhm.get("policyHolderSSN");
		if (!isNull(insuranceSsn) && (!isNumeric(insuranceSsn) || String.valueOf(insuranceSsn).length() != 10)) {
			errors.add("Policy Holder SSN must be a 10 digit number.");
		}

		String policyHolderBirthday = lhm.get("policyHolderBirthday");
		if (!isNull(policyHolderBirthday) && !isValidDate(policyHolderBirthday)) {
			errors.add("Policy Holder Birthday is not valid.");
		}

		return errors;
	}

	// Returns a key-value pairing of the form fields their respective input
	// values
	public static LinkedHashMap<String, String> getFormFieldInputPairs(HttpServletRequest request) {
		String[] formFields = getFormFields();
		int formFieldsCount = formFields.length;

		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		for (int i = 0; i < formFieldsCount; i++) {
			lhm.put(formFields[i], request.getParameter(formFields[i]));
		}

		return lhm;
	}

	public static String[] getFormFields() {
		String[] formFields = { "firstName", "middleName", "lastName", "birthday", "ssn", "sex", "address1", "address2",
				"city", "state", "zip", "cellPhone", "homePhone", "workPhone", "email", "emergencyName",
				"emergencyRelationship", "emergencyNumber", "insuranceName", "insuranceAddress", "insuranceID",
				"insuranceGroup", "insuranceCopay", "effectiveDate", "policyHolderName", "policyHolderSSN",
				"policyHolderBirthday" };
		return formFields;
	}

	private static boolean isValidDate(String dateString) {
		boolean isDate = false;
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		format.setLenient(false);
		try {
			if (dateString.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) {
				Date date = format.parse(dateString);
				isDate = true;
			}
		} catch (ParseException pe) {
			return false;
		}
		return isDate;
	}

	// Returns true if every character is white space, or null
	private static boolean isNull(String phrase) {
		if (phrase == null)
			return true;

		for (int i = 0; i < phrase.length(); i++)
			if (!Character.isWhitespace(phrase.charAt(i)))
				return false;

		return true;
	}

	private static boolean isNumeric(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
