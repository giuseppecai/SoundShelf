package supporto;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/informazioniControl")
public class InformazioniControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private SupportRequestDAO supportRequestDAO;

    @Override
    public void init() throws ServletException {
        supportRequestDAO = new SupportRequestDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idRichiesta = request.getParameter("idRichiesta");
        String informazioniAggiuntive = request.getParameter("informazioniAggiuntive");
        String rispostaUtente = request.getParameter("rispostaUtente");

        if (idRichiesta == null || idRichiesta.isEmpty()) {
            request.setAttribute("errorMessage", "Id della richiesta non valido.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        try {
            SupportRequest supportRequest = supportRequestDAO.getSupportRequestById(Integer.parseInt(idRichiesta));

            if (supportRequest != null) {
            	
                if (informazioniAggiuntive != null && !informazioniAggiuntive.isEmpty()) {
                    supportRequest.setInformazioniAggiuntive(informazioniAggiuntive);
                    supportRequest.setRispostaUtente(null);
                }
                
                if (rispostaUtente != null && !rispostaUtente.isEmpty() && supportRequest.getInformazioniAggiuntive() != null) {
                    supportRequest.setRispostaUtente(rispostaUtente);
                    supportRequest.setStato(StatoSupporto.IN_LAVORAZIONE);
                } else if (rispostaUtente != null && !rispostaUtente.isEmpty()) {
                    request.setAttribute("errorMessage", "Non puoi rispondere prima che il gestore invii informazioni aggiuntive.");
                    request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                    return;
                }

                supportRequestDAO.updateSupportRequest(supportRequest);
                response.sendRedirect("home");
            } else {
                request.setAttribute("errorMessage", "La richiesta non esiste.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore nell'aggiornamento.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Id della richiesta non valido.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}

