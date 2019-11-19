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
import java.io.PrintWriter;

@WebServlet("/ViewAppointments")
public class ViewAppointments extends HttpServlet 
{
    private static final long serialVersionUID = 1L;
    /**
     * Retrieves all appointments for view_appointments.jsp
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    { 
        String dateFilter = request.getParameter("dateFilter");
        
        if (dateFilter == null) {
            dateFilter = "Today";
        }
        
        ArrayList<ArrayList<String>> aList = Appointment.getByFilter(dateFilter);
        
        request.setAttribute("arrayList", aList);
        request.setAttribute("dateFilter", dateFilter);
        request.getRequestDispatcher("view_appointments.jsp").include(request, response);
    }
    
    /**
     * Retrieves a single appointment's information pertaining to a
     * particular patient.
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    }	
}