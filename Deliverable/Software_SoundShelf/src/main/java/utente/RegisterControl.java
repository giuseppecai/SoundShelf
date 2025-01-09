package utente;

import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/register")
public class RegisterControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UtenteRegistratoDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UtenteRegistratoDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = InputSanitizer.sanitize(request.getParameter("email"));
        String nome = InputSanitizer.sanitize(request.getParameter("nome"));
        String password = InputSanitizer.sanitize(request.getParameter("password"));
        String cognome = InputSanitizer.sanitize(request.getParameter("cognome"));
        String via = InputSanitizer.sanitize(request.getParameter("via"));
        String cap = InputSanitizer.sanitize(request.getParameter("cap"));
        String numero = InputSanitizer.sanitize(request.getParameter("numero"));
        String provincia = InputSanitizer.sanitize(request.getParameter("provincia"));
        String citta = InputSanitizer.sanitize(request.getParameter("citta"));
        String telefono = InputSanitizer.sanitize(request.getParameter("telefono"));
        
        String indirizzo = via + ", " + numero + ", " + cap + ", " + citta + ", " + provincia;

        String hashedPassword = hashPassword(password);
        
        if (userDAO.getUserByEmail(email) == null) {
        	UtenteRegistrato newUser = new UtenteRegistrato(email, hashedPassword, nome, cognome, indirizzo, telefono, Ruolo.UTENTE);
        	userDAO.addUser(newUser);
        	response.sendRedirect(request.getContextPath() + "/home");
        }
        else {
        	request.setAttribute("errorMessage", "Hai già creato un account con questa mail. Scegline una nuova.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
    
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
