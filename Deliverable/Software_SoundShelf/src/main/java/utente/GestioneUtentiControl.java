package utente;

import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/gestisciCatalogoUtentiControl")
public class GestioneUtentiControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteRegistratoDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UtenteRegistratoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<UtenteRegistrato> users = userDAO.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("view/utenteInterface/catalogoUtenti.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il recupero degli utenti.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = InputSanitizer.sanitize(request.getParameter("email"));
        String action = InputSanitizer.sanitize(request.getParameter("action"));

        try {
            if ("delete".equals(action)) {
                userDAO.deleteUser(email);
            } else if ("promote".equals(action)) {
                userDAO.promoteToAdmin(email);
            }
            response.sendRedirect("gestisciCatalogoUtentiControl");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante l'operazione sugli utenti.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}
