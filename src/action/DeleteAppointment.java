package action;

import java.io.IOException;
import java.util.ArrayList;
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

@WebServlet("/DeleteAppointment")
public class DeleteAppointment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("appointmentID");

		List<String> patientSSN = Appointment.getSSNFromId(Integer.parseInt(id));
		long ssn = Long.parseLong(patientSSN.get(0));
		
		// Retrieve patient information
		Map<String, String> patientInformation = Patient.getAttributeValuePairsBySSN(ssn);
		for (Map.Entry<String, String> field : patientInformation.entrySet()) {
			request.setAttribute(field.getKey(), field.getValue());
		}

		try {
			Appointment.delete(Integer.parseInt(id));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// Retrieve appointment information
		ArrayList<ArrayList<String>> upcomingAppts = Appointment.getBySSNAndTimePeriod(ssn,
				"upcoming");
		ArrayList<ArrayList<String>> pastAppts = Appointment.getBySSNAndTimePeriod(ssn,
				"past");

		request.setAttribute("ssn", ssn);
		request.setAttribute("upcomingAppts", upcomingAppts);
		request.setAttribute("pastAppts", pastAppts);

		String tag = request.getParameter("tag");
		if(tag.contentEquals("fromAppointment")) {
			RequestDispatcher rd = request.getRequestDispatcher("ViewAppointments");			
			request.setAttribute("bannerMessage", "Success! Appointment has been updated.");
			rd.include(request, response);
		}
		else {
			RequestDispatcher rd = request.getRequestDispatcher("view_patient.jsp");			
			request.setAttribute("bannerMessage", "Success! Appointment has been updated.");
			rd.include(request, response);
		}
	}
}
