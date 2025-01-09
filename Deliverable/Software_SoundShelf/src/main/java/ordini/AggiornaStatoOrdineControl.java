package ordini;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/aggiornaStatoOrdineControl")
public class AggiornaStatoOrdineControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int numeroOrdine = Integer.parseInt(request.getParameter("numeroOrdine"));
            String nuovoStato = request.getParameter("nuovoStato");
            System.out.println(nuovoStato);
            StatoOrdine statoOrdine = StatoOrdine.fromString(nuovoStato);
            System.out.println(statoOrdine.getStato());
            boolean success = orderDAO.updateOrderStatus(numeroOrdine, statoOrdine.getStato());

            if (!success) {
                request.setAttribute("errorMessage", "Errore durante l'aggiornamento dello stato ordine.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
            response.sendRedirect(request.getContextPath() + "/gestisciCatalogoOrdiniControl");

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore: " + e.getMessage());
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
        
    }
}
