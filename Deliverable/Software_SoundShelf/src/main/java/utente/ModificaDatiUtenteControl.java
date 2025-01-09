package utente;

import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/profileControl")
public class ModificaDatiUtenteControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UtenteRegistratoDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UtenteRegistratoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/view/utenteInterface/loginForm.jsp");
            return;
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("view/utenteInterface/profileView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/view/utenteInterface/loginForm.jsp");
            return;
        }

        String nome = InputSanitizer.sanitize(request.getParameter("nome"));
        String cognome = InputSanitizer.sanitize(request.getParameter("cognome"));
        String indirizzo = InputSanitizer.sanitize(request.getParameter("indirizzo"));
        String telefono = InputSanitizer.sanitize(request.getParameter("telefono"));

        boolean valid = true;
        StringBuilder errorMsg = new StringBuilder();

        if (nome == null || nome.isEmpty() || nome.length() > 50) {
            valid = false;
            errorMsg.append("Nome non valido. ");
        }
        if (cognome == null || cognome.isEmpty() || cognome.length() > 50) {
            valid = false;
            errorMsg.append("Cognome non valido. ");
        }
        if (indirizzo == null || indirizzo.isEmpty() || indirizzo.length() > 100) {
            valid = false;
            errorMsg.append("Indirizzo non valido. ");
        }
        if (telefono == null || !telefono.matches("\\+?[0-9]{10,15}")) {
            valid = false;
            errorMsg.append("Numero di telefono non valido. ");
        }

        if (!valid) {
            request.setAttribute("errorMessage", errorMsg.toString());
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        user.setNome(nome);
        user.setCognome(cognome);
        user.setIndirizzo(indirizzo);
        user.setTelefono(telefono);

        userDAO.updateUser(user);

        session.setAttribute("user", user);
        response.sendRedirect(request.getContextPath() + "/profileControl");
    }
}
