package action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Appointment;

@WebServlet("/DeleteAppointment")
public class DeleteAppointment extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("appointmentID");

		try {
			Appointment.delete(Integer.parseInt(id));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("ViewAppointments");
		request.setAttribute("bannerMessage", "Success! Appointment has been deleted.");
		rd.forward(request, response);
	}
}
