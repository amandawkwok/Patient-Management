package helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import entity.Appointment;

public class AppointmentFormHelper {
	public static List<String> validateInput(HttpServletRequest request, long primaryKey) {
		LinkedHashMap<String, String> lhm = getFormFieldInputPairs(request);
		List<String> errors = new ArrayList<String>();
		
		
		// APPOINTMENT
//		if (Validator.isNull(lhm.get("first"))) {
//			errors.add("First Name cannot be left blank.");
//		}
//		
//		if (Validator.isNull(lhm.get("last"))) {
//			errors.add("Last Name cannot be left blank.");
//		}
		
//		if (Validator.isNull(lhm.get("date")) || !DateHelper.isValidDate(lhm.get("date"))) {
//			errors.add("Appointment date must be a valid date in MM/DD/YYYY format.");
//		}
		
		if (Validator.isNull(lhm.get("dayTime")) || !TimeHelper.isValidTime(lhm.get("dayTime"))) {
			errors.add("Appointment time must be a valid time in HH:mm am/pm format.");
		}
		
		return errors;
	}
	
	/**
	 * Returns a key-value pairing of the form fields with their respective
	 * input values.
	 */
	public static LinkedHashMap<String, String> getFormFieldInputPairs(HttpServletRequest request) {
		List<String> formFields = Appointment.getAttributeNames();
		int formFieldsCount = formFields.size();
		
		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		for(int i = 0; i < formFieldsCount; i++) {
			lhm.put(formFields.get(i), request.getParameter(formFields.get(i)));
		}
		
		return lhm;
	}
}
