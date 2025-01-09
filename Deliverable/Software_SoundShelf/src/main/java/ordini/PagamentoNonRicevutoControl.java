package ordini;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PagamentoNonRicevutoControl")
public class PagamentoNonRicevutoControl extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private OrderDAO ordineDAO;

    public PagamentoNonRicevutoControl() {
        super();
        ordineDAO = new OrderDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ordineId = request.getParameter("ordineId");

        Order ordine = ordineDAO.getOrderById(Integer.parseInt(ordineId));

		if (ordine != null) {
		        ordine.setStato(StatoOrdine.ANNULLATO);
		        ordineDAO.updateOrder(ordine);
		} else {
		    request.setAttribute("errorMessage", "Ordine non trovato.");
		    request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
		}

        response.sendRedirect("gestisciCatalogoOrdiniControl");
    }
}
