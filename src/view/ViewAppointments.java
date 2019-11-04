package view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Appointment;
import entity.Patient;

@WebServlet("/ViewAppointments")
public class ViewAppointments extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Retrieves all appointments for view_appointments.jsp
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//String filterClause = request.getParameter("filterClause");
		
		//ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();
		
		
		request.getRequestDispatcher("view_appointments.jsp").include(request, response);
	}
	
	/**
	 * Retrieves a single appointment's information pertaining to a
	 * particular patient.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	
	}	
}