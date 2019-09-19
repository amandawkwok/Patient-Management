package view;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ViewAppointments")
public class ViewAppointments extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// response.setContentType("text/html");
		//
		// PrintWriter out = response.getWriter();
		// out.println("<h1>ViewAppointments-doGet()</h1>");
		request.getRequestDispatcher("view_appointments.jsp").include(request, response);
	}
}