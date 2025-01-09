package utente;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/utenteControl")
public class UtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteRegistratoDAO utenteDAO;

    @Override
    public void init() throws ServletException {
        utenteDAO = new UtenteRegistratoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        if (email != null && !email.isEmpty()) {
            UtenteRegistrato utente = utenteDAO.getUserByEmail(email);
            if (utente != null) {
                request.setAttribute("utente", utente);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/view/utenteInterface/profileView.jsp");
                dispatcher.forward(request, response);
            } else {
            	request.setAttribute("message", "Errore nel recupero dell'utente.");
                response.sendRedirect("view/error/messaggioErrore.jsp");
            }
        } else {
        	request.setAttribute("message", "Errore nel recupero dell'utente.");
            response.sendRedirect("view/error/messaggioErrore.jsp");
        }
    }
}
