package supporto;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/gestisciRichiestaSupportoControl")
public class GestisciRichiestaSupportoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public SupportRequestDAO supportRequestDAO;

    @Override
    public void init() throws ServletException {
        supportRequestDAO = new SupportRequestDAO();
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<SupportRequest> richieste = supportRequestDAO.getAllSupportRequests();
            request.setAttribute("richieste", richieste);
            request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore nel recupero delle richieste di supporto.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("azione");

        if ("richiediInformazioni".equals(action)) {
            String idRichiesta = request.getParameter("richiestaId");
            String informazioniAggiuntive = request.getParameter("informazioniAggiuntive");

            if (idRichiesta == null || idRichiesta.isEmpty() || informazioniAggiuntive == null || informazioniAggiuntive.isEmpty()) {
                request.setAttribute("errorMessage", "ID della richiesta o informazioni aggiuntive non valide.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            try {
                SupportRequest supportRequest = supportRequestDAO.getSupportRequestById(Integer.parseInt(idRichiesta));
                if (supportRequest != null) {
                    supportRequest.setInformazioniAggiuntive(informazioniAggiuntive);
                    supportRequest.setStato(StatoSupporto.ATTESA_INFO);
                    supportRequest.setRispostaUtente(null);
                    supportRequestDAO.updateSupportRequest(supportRequest);
                } else {
                    request.setAttribute("errorMessage", "La richiesta di supporto non esiste.");
                    request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                }

                List<SupportRequest> richieste = supportRequestDAO.getAllSupportRequests();
                request.setAttribute("richieste", richieste);
                request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp").forward(request, response);
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Errore durante l'aggiornamento delle informazioni.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else if ("aggiornaStato".equals(action)) {
            String idRichiesta = request.getParameter("richiestaId");
            String nuovoStato = request.getParameter("nuovoStato");

            if (idRichiesta == null || idRichiesta.isEmpty() || nuovoStato == null || nuovoStato.isEmpty()) {
                request.setAttribute("errorMessage", "ID richiesta o stato non valido.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            try {
                SupportRequest supportRequest = supportRequestDAO.getSupportRequestById(Integer.parseInt(idRichiesta));

                if (supportRequest != null) {
                    StatoSupporto statoEnum = StatoSupporto.fromString(nuovoStato);

                    if (statoEnum != null) {
                        supportRequest.setStato(statoEnum);
                        supportRequestDAO.updateSupportRequest(supportRequest);
                    } else {
                        request.setAttribute("errorMessage", "Stato non valido.");
                        request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                    }
                } else {
                    request.setAttribute("errorMessage", "La richiesta non esiste.");
                    request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                }

                List<SupportRequest> richieste = supportRequestDAO.getAllSupportRequests();
                request.setAttribute("richieste", richieste);
                request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp").forward(request, response);
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Errore nell'aggiornamento dello stato della richiesta.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
        }
    }
}
