package supporto;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utente.UtenteRegistrato;
import utente.UtenteRegistratoDAO;

@WebServlet("/richiestaSupportoControl")
public class RichiestaSupportoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SupportRequestDAO supportRequestDAO;
    private UtenteRegistratoDAO clienteDAO;
    
    @Override
    public void init() throws ServletException {
        supportRequestDAO = new SupportRequestDAO();
        clienteDAO = new UtenteRegistratoDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        String email = request.getParameter("email");
        String name = request.getParameter("name");

        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Il campo descrizione è obbligatorio.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        UtenteRegistrato user = clienteDAO.getUserByEmail(email);
        if (user == null || !user.getNome().equals(name)) {
            request.setAttribute("errorMessage", "Nome ed email non corrispondono.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        java.util.Date date = new java.util.Date();
        Date dataInvio = new Date(date.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String orarioInvio = timeFormat.format(date);

        StatoSupporto stato = StatoSupporto.IN_LAVORAZIONE;

        int idCliente = clienteDAO.getUserIdByEmail(user.getEmail());

        SupportRequest supportRequest = new SupportRequest(description, dataInvio, orarioInvio, stato, idCliente);

        try {
            supportRequestDAO.saveSupportRequest(supportRequest);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Si è verificato un errore nell'invio della richiesta. Riprova.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            e.printStackTrace();
        }
        
        response.sendRedirect("home");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user != null) {
            try {
                List<SupportRequest> richiesteSupporto = supportRequestDAO.getSupportRequestsByEmail(user.getEmail());
                request.setAttribute("richieste", richiesteSupporto);
                request.getRequestDispatcher("view/supportoInterface/richiestaSupportoView.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Errore nel recupero delle richieste di supporto.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("view/utenteInterface/loginForm.jsp").forward(request, response);
        }
    }
}
