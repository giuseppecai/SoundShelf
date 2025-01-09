package ordini;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/gestisciCatalogoOrdiniControl")
public class GestisciCatalogoOrdiniControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private OrderDAO orderDAO;
    private ElementoOrdineDAO orderDetailDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        orderDetailDAO = new ElementoOrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Order> ordini = orderDAO.getAllOrders();
            Map<Integer, List<ElementoOrdine>> ordineDetailsMap = new HashMap<>();
            for (Order ordine : ordini) {
                List<ElementoOrdine> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(ordine.getNumeroOrdine());
                ordineDetailsMap.put(ordine.getNumeroOrdine(), orderDetails);
            }

            request.setAttribute("ordineDetailsMap", ordineDetailsMap);
            request.setAttribute("ordini", ordini);
            request.getRequestDispatcher("view/ordiniInterface/catalogoOrdini.jsp").forward(request, response);
        } catch (Exception e) {
        	e.printStackTrace();
            request.setAttribute("errorMessage", "Errore durante il recupero degli ordini.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String emailCliente = request.getParameter("emailCliente");
            String dataInizioStr = request.getParameter("dataInizio");
            String dataFineStr = request.getParameter("dataFine");

            Date dataInizio = null;
            Date dataFine = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (dataInizioStr != null && !dataInizioStr.isEmpty()) {
                    dataInizio = new Date(sdf.parse(dataInizioStr).getTime());
                }
                if (dataFineStr != null && !dataFineStr.isEmpty()) {
                    dataFine = new Date(sdf.parse(dataFineStr).getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<Order> ordini;
            if (emailCliente != null || dataInizio != null || dataFine != null) {
                ordini = orderDAO.filterOrders(emailCliente, dataInizio, dataFine);
            } else {
                ordini = orderDAO.getAllOrders();
            }

            Map<Integer, List<ElementoOrdine>> ordineDetailsMap = new HashMap<>();
            for (Order ordine : ordini) {
                List<ElementoOrdine> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(ordine.getNumeroOrdine());
                ordineDetailsMap.put(ordine.getNumeroOrdine(), orderDetails);
            }

            request.setAttribute("ordini", ordini);
            request.setAttribute("ordineDetailsMap", ordineDetailsMap);
            request.getRequestDispatcher("view/ordiniInterface/catalogoOrdini.jsp").forward(request, response);

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante il recupero degli ordini.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}
