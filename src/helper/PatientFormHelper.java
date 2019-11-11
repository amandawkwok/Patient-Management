package helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import entity.Patient;

public class PatientFormHelper {
	public static List<String> validateInput(HttpServletRequest request, long primaryKey) {
		LinkedHashMap<String, String> lhm = getFormFieldInputPairs(request);
		List<String> errors = new ArrayList<String>();

		// PATIENT
		String ssn = lhm.get("ssn");

		if (Validator.isNull(ssn) || !Validator.isValidNumeric(ssn) || String.valueOf(ssn).length() != 9) {
			errors.add("Patient SSN must be a 10 digit number.");
		} else {
			if (Patient.exists(Long.parseLong(ssn)) && primaryKey != Long.parseLong(ssn)) {
				errors.add("A patient with that SSN already exists.");
			}
		}

		if (Validator.isNull(lhm.get("first"))) {
			errors.add("First Name cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("last"))) {
			errors.add("Last Name cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("dob")) || !DateHelper.isValidDate(lhm.get("dob"))) {
			errors.add("Patient birthday must be a valid date in MM/DD/YYYY format.");
		}

		// ADDRESS
		if (Validator.isNull(lhm.get("address1"))) {
			errors.add("Address 1 cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("city"))) {
			errors.add("City cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("zip")) || !Validator.isValidNumeric(lhm.get("zip"))) {
			errors.add("Zip code is not a valid number.");
		}

		// CONTACT
		if (Validator.isNull(lhm.get("cellPhone")) || !Validator.isValidNumeric(lhm.get("cellPhone"))) {
			errors.add("Cell phone number is not valid.");
		}
		if (!lhm.get("homePhone").equals("") && !Validator.isValidNumeric(lhm.get("homePhone"))) {
			errors.add("Home phone number is not valid.");
		}

		if (!lhm.get("workPhone").equals("") && !Validator.isValidNumeric(lhm.get("workPhone"))) {
			errors.add("Work phone number is not valid.");
		}

		// EMERGENCY CONTACT
		if (Validator.isNull(lhm.get("emergencyName"))) {
			errors.add("Emergency Contact Name cannot be left blank.");
		}

		if (lhm.get("emergencyNumber").equals("") || !Validator.isValidNumeric(lhm.get("emergencyNumber"))) {
			errors.add("Emergency Contact Number is not valid.");
		}

		// INSURANCE
		if (Validator.isNull(lhm.get("insuranceId"))) {
			errors.add("Insurance ID cannot be left blank.");
		}

		if (!lhm.get("insuranceCopay").equals("") && !Validator.isValidNumeric(lhm.get("insuranceCopay"))) {
			errors.add("Insurance Copay is not a valid number.");
		}

		if (!DateHelper.isValidDate(lhm.get("insuranceEffectiveDate"))) {
			errors.add("Effective Date must be a valid date in MM/DD/YYYY format.");
		}

		if (Validator.isNull(lhm.get("policyHolderName"))) {
			errors.add("Policy Holder Name cannot be left blank.");
		}

		String insuranceSsn = lhm.get("policyHolderSSN");
		if (!Validator.isNull(insuranceSsn)
				&& (!Validator.isValidNumeric(insuranceSsn) || String.valueOf(insuranceSsn).length() != 10)) {
			errors.add("Policy Holder SSN must be a 10 digit number.");
		}

		String policyHolderBirthday = lhm.get("policyHolderBirthday");
		if (!DateHelper.isValidDate(policyHolderBirthday)) {
			errors.add("Policy Holder Birthday must be a valid date in MM/DD/YYYY format.");
		}

		return errors;
	}

	// Returns a key-value pairing of the form fields with their respective
	// input values
	public static LinkedHashMap<String, String> getFormFieldInputPairs(HttpServletRequest request) {
		List<String> formFields = Patient.getAttributeNames();
		int formFieldsCount = formFields.size();

		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		for (int i = 0; i < formFieldsCount; i++) {
			lhm.put(formFields.get(i), request.getParameter(formFields.get(i)));
		}

		return lhm;
	}

}
