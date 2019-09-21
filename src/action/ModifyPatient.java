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

import entity.Address;
import entity.Appointment;
import entity.Contact;
import entity.EmergencyContact;
import entity.Insurance;
import entity.Patient;
import helper.DateHelper;
import helper.PatientFormHelper;
import helper.Validator;

@WebServlet("/ModifyPatient")
public class ModifyPatient extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates/updates a patient from user input taken from patient_form.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");
		long primaryKey = 0;

		if (!Validator.isNull(request.getParameter("primaryKey"))) {
			primaryKey = Long.parseLong(request.getParameter("primaryKey"));
		}

		LinkedHashMap<String, String> lhm = PatientFormHelper.getFormFieldInputPairs(request);
		List<String> errorMessages = PatientFormHelper.validateInput(request, primaryKey);

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
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("patient_form.jsp").include(request, response);

		} else {
			// If edit patient is successful, forward to view_patient.jsp to
			// review updates
			if (pageHeader.equals("Edit")) {
				_editPatient(Long.parseLong(request.getParameter("primaryKey")), lhm);

				String newPrimaryKey = request.getParameter("ssn");
				long newPrimaryKeyNumeric = Long.parseLong(newPrimaryKey);

				// Retrieve patient information
				Map<String, String> patientInformation = PatientFormHelper
						.getFormFieldDatabasePairsBySSN(newPrimaryKeyNumeric);
				for (Map.Entry<String, String> field : patientInformation.entrySet()) {
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

				request.getRequestDispatcher("view_patient.jsp").include(request, response);
			} else {
				_addPatient(lhm);

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
		long homePhone, workPhone, copay, policyHolderSSN;
		try {
			long ssn = Long.parseLong(lhm.get("ssn"));

			// Patient
			Patient.add(ssn, lhm.get("firstName"), lhm.get("middleName"), lhm.get("lastName"),
					DateHelper.convertToSQLDate(lhm.get("birthday")), lhm.get("sex"));

			// Address
			Address.add(ssn, lhm.get("address1"), lhm.get("address2"), lhm.get("city"), lhm.get("state"),
					Integer.parseInt(lhm.get("zip")));
			// Contact
			try {
				homePhone = Long.parseLong(lhm.get("homePhone"));
			} catch (Exception e) {
				homePhone = java.sql.Types.BIGINT;
			}

			try {
				workPhone = Long.parseLong(lhm.get("workPhone"));
			} catch (Exception e) {
				workPhone = java.sql.Types.BIGINT;
			}

			Contact.add(ssn, Long.parseLong(lhm.get("cellPhone")), homePhone, workPhone, lhm.get("email"));

			// Emergency Contact
			EmergencyContact.add(ssn, lhm.get("emergencyName"), lhm.get("emergencyRelationship"),
					Long.parseLong(lhm.get("emergencyNumber")));

			// Insurance
			try {
				copay = Long.parseLong(lhm.get("insuranceCopay"));
			} catch (Exception e) {
				copay = java.sql.Types.BIGINT;
			}

			try {
				policyHolderSSN = Long.parseLong(lhm.get("policyHolderSSN"));
			} catch (Exception e) {
				policyHolderSSN = java.sql.Types.BIGINT;
			}

			Insurance.add(ssn, lhm.get("insuranceName"), lhm.get("insuranceAddress"), lhm.get("insuranceID"),
					lhm.get("insuranceGroup"), copay, DateHelper.convertToSQLDate(lhm.get("effectiveDate")),
					lhm.get("policyHolderName"), policyHolderSSN,
					DateHelper.convertToSQLDate(lhm.get("policyHolderBirthday")));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}

	private void _editPatient(long oldSSN, LinkedHashMap<String, String> lhm) {
		long homePhone, workPhone, copay, policyHolderSSN;
		try {
			// Address
			Address.update(oldSSN, lhm.get("address1"), lhm.get("address2"), lhm.get("city"), lhm.get("state"),
					Integer.parseInt(lhm.get("zip")));

			// Contact
			try {
				homePhone = Long.parseLong(lhm.get("homePhone"));
			} catch (Exception e) {
				homePhone = java.sql.Types.BIGINT;
			}

			try {
				workPhone = Long.parseLong(lhm.get("workPhone"));
			} catch (Exception e) {
				workPhone = java.sql.Types.BIGINT;
			}

			Contact.update(oldSSN, Long.parseLong(lhm.get("cellPhone")), homePhone, workPhone, lhm.get("email"));

			// Emergency Contact
			EmergencyContact.update(oldSSN, lhm.get("emergencyName"), lhm.get("emergencyRelationship"),
					Long.parseLong(lhm.get("emergencyNumber")));

			// Insurance
			try {
				copay = Long.parseLong(lhm.get("insuranceCopay"));
			} catch (Exception e) {
				copay = java.sql.Types.BIGINT;
			}

			try {
				policyHolderSSN = Long.parseLong(lhm.get("policyHolderSSN"));
			} catch (Exception e) {
				policyHolderSSN = java.sql.Types.BIGINT;
			}

			Insurance.update(oldSSN, lhm.get("insuranceName"), lhm.get("insuranceAddress"), lhm.get("insuranceID"),
					lhm.get("insuranceGroup"), copay, DateHelper.convertToSQLDate(lhm.get("effectiveDate")),
					lhm.get("policyHolderName"), policyHolderSSN,
					DateHelper.convertToSQLDate(lhm.get("policyHolderBirthday")));

			// Patient
			Patient.update(oldSSN, Long.parseLong(lhm.get("ssn")), lhm.get("firstName"), lhm.get("middleName"),
					lhm.get("lastName"), DateHelper.convertToSQLDate(lhm.get("birthday")), lhm.get("sex"));
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
}