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
		Long ssnObject = Long.parseLong(ssn);
		long ssnPrimitive = ssnObject.longValue();

		try {
			Patient.delete(ssnPrimitive);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		// repopulate ViewPatient.jsp
		RequestDispatcher rd = request.getRequestDispatcher("ViewPatients");
		rd.forward(request, response);
	}
}
