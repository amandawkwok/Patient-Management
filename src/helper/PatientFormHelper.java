package helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import entity.Address;
import entity.Contact;
import entity.EmergencyContact;
import entity.Insurance;
import entity.Patient;

public class PatientFormHelper {
	public static List<String> validateInput(HttpServletRequest request) {
		LinkedHashMap<String, String> lhm = getFormFieldInputPairs(request);
		List<String> errors = new ArrayList<String>();

		// PATIENT
		if (Validator.isNull(lhm.get("firstName"))) {
			errors.add("First Name cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("lastName"))) {
			errors.add("Last Name cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("birthday")) || !DateHelper.isValidDate(lhm.get("birthday"))) {
			errors.add("Patient birthday must be a valid date in MM/DD/YYYY format.");
		}

		String ssn = lhm.get("ssn");

		if (Validator.isNull(ssn) || !Validator.isNumeric(ssn) || String.valueOf(ssn).length() != 10) {
			errors.add("Patient SSN must be a 10 digit number.");
		} else {
			if (Patient.exists(Long.parseLong(ssn))) {
				errors.add("A patient with that SSN already exists.");
			}
		}

		// ADDRESS
		if (Validator.isNull(lhm.get("address1"))) {
			errors.add("Address 1 cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("city"))) {
			errors.add("City cannot be left blank.");
		}

		if (Validator.isNull(lhm.get("zip")) || !Validator.isNumeric(lhm.get("zip"))) {
			errors.add("Zip code is not a valid number.");
		}

		// CONTACT
		if (Validator.isNull(lhm.get("cellPhone")) || !Validator.isNumeric(lhm.get("cellPhone"))) {
			errors.add("Cell phone number is not valid.");
		}

		if (!lhm.get("homePhone").equals("") && !Validator.isNumeric(lhm.get("homePhone"))) {
			errors.add("Home phone number is not valid.");
		}

		if (!lhm.get("workPhone").equals("") && !Validator.isNumeric(lhm.get("workPhone"))) {
			errors.add("Work phone number is not valid.");
		}

		// EMERGENCY CONTACT
		if (Validator.isNull(lhm.get("emergencyName"))) {
			errors.add("Emergency Contact Name cannot be left blank.");
		}

		if (lhm.get("emergencyNumber").equals("") || !Validator.isNumeric(lhm.get("emergencyNumber"))) {
			errors.add("Emergency Contact Number is not valid.");
		}

		// INSURANCE
		if (Validator.isNull(lhm.get("insuranceID"))) {
			errors.add("Insurance ID cannot be left blank.");
		}

		if (!lhm.get("insuranceCopay").equals("") && !Validator.isNumeric(lhm.get("insuranceCopay"))) {
			errors.add("Insurance Copay is not a valid number.");
		}

		if (!DateHelper.isValidDate(lhm.get("effectiveDate"))) {
			errors.add("Effective Date must be a valid date in MM/DD/YYYY format.");
		}

		if (Validator.isNull(lhm.get("policyHolderName"))) {
			errors.add("Policy Holder Name cannot be left blank.");
		}

		String insuranceSsn = lhm.get("policyHolderSSN");
		if (!Validator.isNull(insuranceSsn)
				&& (!Validator.isNumeric(insuranceSsn) || String.valueOf(insuranceSsn).length() != 10)) {
			errors.add("Policy Holder SSN must be a 10 digit number.");
		}

		String policyHolderBirthday = lhm.get("policyHolderBirthday");
		if (!Validator.isNull(policyHolderBirthday) && !DateHelper.isValidDate(policyHolderBirthday)) {
			errors.add("Policy Holder Birthday must be a valid date in MM/DD/YYYY format.");
		}

		return errors;
	}

	// Returns a key-value pairing of the form fields with their respective
	// input
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

	// Returns a key-value pairing of the form fields with their respective
	// database values
	public static Map<String, String> getFormFieldDatabasePairsBySSN(long ssn) {
		Map<String, String> map = new LinkedHashMap<String, String>();

		String[] formFields = getFormFields();
		List<String> databaseValues = new ArrayList<String>();
		int listSize = databaseValues.size();

		// Retrieving Patient information
		databaseValues = Patient.getBySSN(ssn);
		for (int i = 1; i < 5; i++) {
			map.put(formFields[i - 1], databaseValues.get(i));
		}
		map.put(formFields[4], databaseValues.get(0));
		map.put(formFields[5], databaseValues.get(5));

		// Retrieving Address information
		databaseValues = Address.getBySSN(ssn);
		listSize = databaseValues.size();
		for (int i = 1; i < listSize; i++) {
			map.put(formFields[i + 5], databaseValues.get(i));
		}

		// Retrieving Contact information
		databaseValues = Contact.getBySSN(ssn);
		listSize = databaseValues.size();
		for (int i = 1; i < listSize; i++) {
			map.put(formFields[i + 10], databaseValues.get(i));
		}

		// Retrieving Emergency Contact information
		databaseValues = EmergencyContact.getBySSN(ssn);
		listSize = databaseValues.size();
		for (int i = 1; i < listSize; i++) {
			map.put(formFields[i + 14], databaseValues.get(i));
		}

		// Retrieving Insurance information
		databaseValues = Insurance.getBySSN(ssn);
		listSize = databaseValues.size();
		for (int i = 1; i < listSize; i++) {
			map.put(formFields[i + 17], databaseValues.get(i));
		}

		return map;
	}

	public static void main(String[] args) {
		System.out.println(getFormFieldDatabasePairsBySSN(888));
	}
}
