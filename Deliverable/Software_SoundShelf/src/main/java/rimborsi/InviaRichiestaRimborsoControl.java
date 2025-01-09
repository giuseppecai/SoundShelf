package rimborsi;

import ordini.ElementoOrdine;
import ordini.ElementoOrdineDAO;
import ordini.Order;
import ordini.OrderDAO;
import prodotti.ProductDAO;
import utente.UtenteRegistrato;
import utente.UtenteRegistratoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/inviaRichiestaRimborsoControl")
public class InviaRichiestaRimborsoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ProductDAO productDAO;
    public ElementoOrdineDAO elementoOrdineDAO;
    public RefoundRequestDAO refoundRequestDAO;
    public UtenteRegistratoDAO utenteDAO;

    @Override
    public void init() throws ServletException {
    	productDAO = new ProductDAO();
        elementoOrdineDAO = new ElementoOrdineDAO();
        refoundRequestDAO = new RefoundRequestDAO();
        utenteDAO = new UtenteRegistratoDAO();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("errorMessage", "Devi essere loggato per richiedere un rimborso.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }
        
        String detailCode = request.getParameter("detailCode");

        if (detailCode != null && !detailCode.isEmpty()) {
            int detailId = Integer.parseInt(detailCode);
            ElementoOrdine detail = elementoOrdineDAO.getOrderDetailsById(detailId);

            if (detail != null) {
            	request.setAttribute("orderDetail", detail);
                request.getRequestDispatcher("/view/rimborsiInterface/richiestaRimborsoForm.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Dettagli prodotto non trovati.");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Parametro non valido.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            request.setAttribute("errorMessage", "Devi essere loggato per richiedere un rimborso.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        String orderDetailID = request.getParameter("orderDetailID");
        String reason = request.getParameter("reason");
        String iban = request.getParameter("iban");

        // Verifica che i parametri non siano nulli o vuoti
        if (orderDetailID == null || orderDetailID.isEmpty() || reason == null || reason.isEmpty() || iban == null || iban.isEmpty()) {
            request.setAttribute("errorMessage", "Tutti i campi sono obbligatori.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        // Verifica che orderDetailID sia un intero valido
        int orderDetailId = -1;
        try {
            orderDetailId = Integer.parseInt(orderDetailID);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "ID dettaglio ordine non valido.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        // Verifica che l'IBAN sia valido (in questo caso, un semplice controllo di formato)
        if (!isValidIban(iban)) {
            request.setAttribute("errorMessage", "IBAN non valido.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        ElementoOrdine product = elementoOrdineDAO.getOrderDetailsById(orderDetailId);

        if (product != null) {
            // Tutto è valido, si crea la richiesta di rimborso
            RefoundRequest refundRequest = new RefoundRequest();
            refundRequest.setIdOrdine(product.getIdOrdine());
            refundRequest.setIdProdotto(product.getIdProdotto());
            refundRequest.setEmailCliente(user.getEmail());
            refundRequest.setMotivo(reason);
            refundRequest.setIban(iban);
            refundRequest.setStato(StatoRimborso.IN_REVISIONE);

            try {
                refoundRequestDAO.saveRichiestaRimborso(refundRequest);
                request.getRequestDispatcher("listaOrdiniUtente").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore nel salvataggio della richiesta di rimborso.");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Prodotto non trovato.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    // Metodo per validare l'IBAN (utilizzo di una regex semplificata)
    private boolean isValidIban(String iban) {
        String regex = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4}[A-Z0-9]{0,30}$"; // Pattern IBAN base
        return iban != null && iban.matches(regex);
    }

}
