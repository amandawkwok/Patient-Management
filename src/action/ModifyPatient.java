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
import helper.PatientFormHelper;

@WebServlet("/ModifyPatient")
public class ModifyPatient extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates/updates a patient from user input taken from patient_form.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");

		List<String> errorMessages = PatientFormHelper.validateInput(request);

		if (errorMessages.size() != 0) {

			if (pageHeader.equals("Edit")) {
				long primaryKey = Long.parseLong(request.getParameter("primaryKey"));
				request.setAttribute("primaryKey", primaryKey);
			}

			// Call helper method to repopulate fields after failed submission
			LinkedHashMap<String, String> lhm = PatientFormHelper.getFormFieldInputPairs(request);
			for (String key : lhm.keySet()) {
				String value = lhm.get(key);
				request.setAttribute(key, value);
			}

			request.setAttribute("pageHeader", pageHeader);
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("patient_form.jsp").include(request, response);

		} else {
			// If edit patient is successful, forward to view_patient.jsp to
			// review updates
			if (pageHeader.equals("Edit")) {
				// _editPatient(lhm);

				String primaryKey = request.getParameter("ssn");
				long primaryKeyNumeric = Long.parseLong(primaryKey);

				// Retrieve patient information
				Map<String, String> patientInformation = PatientFormHelper
						.getFormFieldDatabasePairsBySSN(primaryKeyNumeric);

				for (Map.Entry<String, String> field : patientInformation.entrySet()) {
					request.setAttribute(field.getKey(), field.getValue());
				}

				// Retrieve appointment information
				ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(primaryKeyNumeric,
						"upcoming");
				ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(primaryKeyNumeric, "past");

				request.setAttribute("primaryKey", primaryKey);
				request.setAttribute("upcomingAppts", upcomingAppts);
				request.setAttribute("pastAppts", pastAppts);

				request.getRequestDispatcher("view_patient.jsp").include(request, response);
			} else {
				// _addPatient(lhm);

				// forward to view_patients.jsp to view all patients
				RequestDispatcher rd = request.getRequestDispatcher("ViewPatients");
				rd.forward(request, response);
			}
		}
	}

	/**
	 * Retrieves patient information to be loaded into patient_form.jsp
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String primaryKey = request.getParameter("primaryKey");

		Map<String, String> patientInformation = PatientFormHelper
				.getFormFieldDatabasePairsBySSN(Long.parseLong(primaryKey));

		for (Map.Entry<String, String> field : patientInformation.entrySet()) {
			request.setAttribute(field.getKey(), field.getValue());
		}

		request.setAttribute("pageHeader", "Edit");
		request.setAttribute("primaryKey", primaryKey);

		request.getRequestDispatcher("patient_form.jsp").include(request, response);
	}

	private void _addPatient(LinkedHashMap<String, String> lhm) {

	}

	private void _editPatient(LinkedHashMap<String, String> lhm) {

	}
}