package helper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import entity.Appointment;

public class AppointmentFormHelper {
	public static List<String> validateInput(HttpServletRequest request) {
		LinkedHashMap<String, String> lhm = getFormFieldInputPairs(request);
		List<String> errors = new ArrayList<String>();
		System.out.println("dayTime: " +lhm.get("dayTime"));
		String[] dayAndTime = lhm.get("dayTime").split(" ");
		String date = dayAndTime[0];
		String time = dayAndTime[1] + " " + dayAndTime[2];
		System.out.println("date: " + date);
		System.out.println("time: " + time);
		
		// APPOINTMENT
		if (Validator.isNull(date) || !DateHelper.isValidDate(date)) {
			errors.add("Appointment date must be a valid date in MM/DD/YYYY format.");
		}
		
		if (Validator.isNull(time) || !TimeHelper.isValidTime(time)) {
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
		System.out.println("formfields: ");
		for(int i=0;i<formFieldsCount;i++){
		    System.out.println(formFields.get(i));
		} 
		System.out.println();
		
		LinkedHashMap<String, String> lhm = new LinkedHashMap<String, String>();
		for(int i = 0; i < formFieldsCount; i++) {
			lhm.put(formFields.get(i), request.getParameter(formFields.get(i)));
		}
		lhm.put("dayTime", (String) request.getAttribute("dayTime"));
		
		return lhm;
	}
}
