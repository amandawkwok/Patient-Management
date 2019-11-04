package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Appointment;
import entity.Patient;
import helper.AppointmentFormHelper;
import helper.DateHelper;
import helper.PatientFormHelper;
import helper.TimeHelper;
import helper.Validator;

@WebServlet("/ModifyAppointment")
public class ModifyAppointment extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Creates/updates an appointment from user input taken from appointment_form.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");
		long primaryKey = 0;
		
		if (!Validator.isNull(request.getParameter("primaryKey"))) {
			primaryKey = Long.parseLong(request.getParameter("primaryKey"));
		}
		
		LinkedHashMap<String, String> lhm = AppointmentFormHelper.getFormFieldInputPairs(request);
		List<String> errorMessages = AppointmentFormHelper.validateInput(request, primaryKey);
		
		if (errorMessages.size() != 0) {
			if (pageHeader.equals("Edit")) {
				request.setAttribute("primaryKey", primaryKey);
			}
			
			// Call helper method to repopulate fields after failed submission
			for (String key : lhm.keySet()) {
				String value = lhm.get(key);
				request.setAttribute(key, value);
			}
			
			request.setAttribute("pageHeader", pageHeader);
			request.setAttribute("errorMessages",  errorMessages);
			request.getRequestDispatcher("appointment_form.jsp").include(request, response);
		}
		else {
			// if edit appointment is successful, forward to view_patient.jsp to
			// review updates
			if (pageHeader.contentEquals("Edit")) {
				editAppointment(Long.parseLong(request.getParameter("primaryKey")), lhm);
				
				String newPrimaryKey = request.getParameter("primaryKey");
				long newPrimaryKeyNumeric = Long.parseLong(newPrimaryKey);
				
				// Retrieve appointment information
				Map<String, String> apptInformation = Appointment.getAttributeValuePairsbyId(newPrimaryKeyNumeric);
				for (Map.Entry<String, String> field : apptInformation.entrySet()) {
					request.setAttribute(field.getKey(), field.getValue());
				}
				
				// Retrieve appointment information
				ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(newPrimaryKeyNumeric,
						"upcoming");
				ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(newPrimaryKeyNumeric,
						"past");
				
				request.setAttribute("primaryKey", newPrimaryKeyNumeric);
				request.setAttribute("upcomingAppts", upcomingAppts);
				request.setAttribute("pastAppts", pastAppts);
				request.setAttribute("first", request.getParameter("first"));
				request.setAttribute("middle", request.getParameter("middle"));
				request.setAttribute("last", request.getParameter("last"));
				request.setAttribute("pageHeader", pageHeader);
				request.setAttribute("bannerMessage", "Success! Appointment has been updated.");
				
				request.getRequestDispatcher("appointment_form.jsp").include(request, response);
			}
			else {
				addAppointment(lhm);

				// forward to view_patients.jsp to view all patients
				RequestDispatcher rd = request.getRequestDispatcher("view_patient.jsp");
				request.setAttribute("bannerMessage", "Success! Appointment has been added.");
				request.setAttribute("primaryKey", Long.parseLong(request.getParameter("primaryKey")));
				rd.include(request, response);
			}
		}
	}
	
	/**
	 * Retrieves appointment information to be loaded into appointment_form.jsp 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String primaryKey = request.getParameter("primaryKey");

		Map<String, String> apptInformation = Appointment.getAttributeValuePairsbyId(Long.parseLong(primaryKey));
		
		for (Map.Entry<String, String> field : apptInformation.entrySet()) {
			request.setAttribute(field.getKey(), field.getValue());
		}
		
		request.setAttribute("pageHeader", "Edit");
		request.setAttribute("primaryKey", primaryKey);
	}

	private void addAppointment(LinkedHashMap<String, String> lhm) {
		try {
			long id = Long.parseLong(lhm.get("id"));
			
			Appointment.add(id, lhm.get("first"), lhm.get("middle"), lhm.get("last"), 
					DateHelper.convertToSQLDate(lhm.get("date")), 
					TimeHelper.convertToSQLTime(lhm.get("dayTime")), 
					lhm.get("status"), lhm.get("reason"));
			
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
		
	private void editAppointment(long oldId, LinkedHashMap<String, String> lhm) {
		try {
			Appointment.update(oldId, Long.parseLong(lhm.get("id")), 
					lhm.get("first"), lhm.get("middle"), lhm.get("last"), 
					DateHelper.convertToSQLDate(lhm.get("date")), 
					TimeHelper.convertToSQLTime(lhm.get("dayTime")), 
					lhm.get("status"), lhm.get("reason"));
			
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
