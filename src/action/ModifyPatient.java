package action;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ModifyPatient")
public class ModifyPatient extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates/updates a patient
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageHeader = request.getParameter("pageHeader");

		// Validate
		List<String> errorMessages = PatientFormHelper.validateInput(request);

		if (errorMessages.size() != 0) {

			if (pageHeader.equals("Edit")) {
				Long primaryKey = Long.parseLong(request.getParameter("primaryKey"));
				System.out.println(primaryKey);
				request.setAttribute("primaryKey", primaryKey);
			}

			// Call helper method to repopulate fields after failed submission
			LinkedHashMap<String, String> lhm = PatientFormHelper.getFormFieldInputPairs(request);
			for (String key : lhm.keySet()) {
				String value = lhm.get(key);
				request.setAttribute(key, value);
				// System.out.println(key + " : " + value);
			}

			request.setAttribute("pageHeader", pageHeader);
			request.setAttribute("errorMessages", errorMessages);
			request.getRequestDispatcher("patient_form.jsp").include(request, response);

		} else {
			RequestDispatcher rd = request.getRequestDispatcher("ViewPatients");
			rd.forward(request, response);
		}
	}

	/**
	 * Retrieves patient information to be loaded into patient_form.jsp
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("pageHeader", "Edit");
		request.setAttribute("primaryKey", request.getParameter("primaryKey"));
		System.out.println("ModifyPatient DOPOST: " + request.getParameter("primaryKey"));

		request.getRequestDispatcher("patient_form.jsp").include(request, response);
	}
}