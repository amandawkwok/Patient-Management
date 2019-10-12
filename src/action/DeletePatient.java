package action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.Patient;

@WebServlet("/DeletePatient")
public class DeletePatient extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ssn = request.getParameter("primaryKey");

		try {
			Patient.delete(Long.parseLong(ssn));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("ViewPatients");
		request.setAttribute("bannerMessage", "Success! Patient has been deleted.");
		rd.forward(request, response);
	}
}
