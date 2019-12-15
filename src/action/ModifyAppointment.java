package action;

import java.io.IOException;
import java.sql.Timestamp;
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
import helper.TimeHelper;

@WebServlet("/ModifyAppointment")
public class ModifyAppointment extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates/updates an appointment from user input taken from
	 * appointment_form.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");

		String date = request.getParameter("date");
		String time = request.getParameter("time");
		String dayTime = date + " " + time;
		request.setAttribute("dayTime", dayTime);

		LinkedHashMap<String, String> lhm = AppointmentFormHelper.getFormFieldInputPairs(request);

		if (pageHeader.contentEquals("Edit")) {
			lhm.put("ssn", "" + postSSN);
		}

		List<String> errorMessages = AppointmentFormHelper.validateInput(request);

		if (errorMessages.size() != 0) {
			long ssn = 0L;

			if (pageHeader.equals("Edit")) {
				ssn = postSSN;
			} else {
				ssn = Long.parseLong(request.getParameter("ssn"));
			}

			Map<String, String> patientName = Patient.getAttributeValuePairsBySSN(ssn);

			for (Map.Entry<String, String> field : patientName.entrySet()) {
				request.setAttribute(field.getKey(), field.getValue());
			}

			for (String key : lhm.keySet()) {
				String value = lhm.get(key);
				request.setAttribute(key, value);
			}
			request.setAttribute("date", date);
			request.setAttribute("time", time);

			request.setAttribute("pageHeader", pageHeader);
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("dateFilter", request.getParameter("dateFilter"));
			request.getRequestDispatcher("appointment_form.jsp").include(request, response);
		} else {
			// if edit appointment is successful, forward to view_patient.jsp to
			// review updates
			if (pageHeader.contentEquals("Edit")) {
				editAppointment(lhm);

				long pastSSNLong = postSSN;

				// Retrieve patient information
				Map<String, String> patientInformation = Patient.getAttributeValuePairsBySSN(pastSSNLong);
				for (Map.Entry<String, String> field : patientInformation.entrySet()) {
					request.setAttribute(field.getKey(), field.getValue());
				}

				// Retrieve appointment information
				ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(pastSSNLong, "upcoming");
				ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(pastSSNLong, "past");

				request.setAttribute("ssn", pastSSNLong);
				request.setAttribute("upcomingAppts", upcomingAppts);
				request.setAttribute("pastAppts", pastAppts);
				request.setAttribute("dateFilter", request.getParameter("dateFilter"));

				String tag = request.getParameter("tag");
				if (tag.contentEquals("fromAppointment")) {
					RequestDispatcher rd = request.getRequestDispatcher("ViewAppointments");
					request.setAttribute("dateFilter", request.getParameter("dateFilter"));
					request.setAttribute("bannerMessage", "Success! Appointment has been updated.");
					rd.include(request, response);
				} else {
					RequestDispatcher rd = request.getRequestDispatcher("view_patient.jsp");
					request.setAttribute("bannerMessage", "Success! Appointment has been updated.");
					rd.include(request, response);
				}

			} else {
				addAppointment(lhm);

				long pastSSNLong = Long.parseLong(lhm.get("ssn"));

				// Retrieve patient information
				Map<String, String> patientInformation = Patient.getAttributeValuePairsBySSN(pastSSNLong);
				for (Map.Entry<String, String> field : patientInformation.entrySet()) {
					request.setAttribute(field.getKey(), field.getValue());
				}

				// Retrieve appointment information
				ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(pastSSNLong, "upcoming");
				ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(pastSSNLong, "past");

				request.setAttribute("ssn", pastSSNLong);
				request.setAttribute("upcomingAppts", upcomingAppts);
				request.setAttribute("pastAppts", pastAppts);

				// go to view_patient.jsp to view patient which the appt belongs
				// to
				RequestDispatcher rd = request.getRequestDispatcher("view_patient.jsp");
				request.setAttribute("bannerMessage", "Success! Appointment has been added.");
				rd.include(request, response);
			}
		}
	}

	/**
	 * Retrieves appointment information to be loaded into appointment_form.jsp
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");

		if (pageHeader.contentEquals("Edit")) {
			int appointmentId = Integer.parseInt(request.getParameter("appointmentID"));

			List<String> patientSSN = Appointment.getSSNFromId(appointmentId);
			long ssn = Long.parseLong(patientSSN.get(0));

			Map<String, String> pat = Patient.getAttributeValuePairsBySSN(ssn);
			for (Map.Entry<String, String> field : pat.entrySet()) {
				request.setAttribute(field.getKey(), field.getValue());
			}

			Map<String, String> appt = Appointment.getAttributeValuePairsById(appointmentId);

			for (Map.Entry<String, String> field : appt.entrySet()) {
				request.setAttribute(field.getKey(), field.getValue());
			}
			String dayTime = appt.get("dayTime");
			String[] dayAndTime = dayTime.split(" ");
			String date = dayAndTime[0];
			String time = dayAndTime[1] + " " + dayAndTime[2];
			request.setAttribute("date", date);
			request.setAttribute("time", time);
			request.setAttribute("primaryKey", ssn);
			request.setAttribute("dateFilter", request.getParameter("dateFilter"));

			postSSN = ssn;
			postID = appointmentId;

			request.setAttribute("pageHeader", "Edit");

			request.getRequestDispatcher("appointment_form.jsp").include(request, response);
		} else if (pageHeader.contentEquals("Add")) {
			long ssn = Long.parseLong(request.getParameter("ssn"));

			Map<String, String> patientInformation = Patient.getAttributeValuePairsBySSN(ssn);

			for (Map.Entry<String, String> field : patientInformation.entrySet()) {
				request.setAttribute(field.getKey(), field.getValue());
			}

			request.setAttribute("pageHeader", "Add");

			request.getRequestDispatcher("appointment_form.jsp").include(request, response);
		}
	}

	private void addAppointment(LinkedHashMap<String, String> lhm) {
		try {
			long ssn = Long.parseLong(lhm.get("ssn"));

			String dayTimeString = lhm.get("dayTime");
			Timestamp dayTime = TimeHelper.convertToSQLDateTime(dayTimeString);

			Appointment.add(ssn, dayTime, lhm.get("status"), lhm.get("reason"));

		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private void editAppointment(LinkedHashMap<String, String> lhm) {
		try {
			String dayTimeString = lhm.get("dayTime");
			Timestamp dayTime = TimeHelper.convertToSQLDateTime(dayTimeString);

			int id = postID;

			Appointment.update(id, dayTime, lhm.get("status"), lhm.get("reason"));

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private int postID;
	private long postSSN;
}
