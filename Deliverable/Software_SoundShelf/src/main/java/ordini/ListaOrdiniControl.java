package ordini;

import prodotti.Product;
import prodotti.ProductDAO;
import utente.UtenteRegistrato;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/listaOrdiniUtente")
public class ListaOrdiniControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderDAO ordineDAO;
    private ProductDAO prodottoDAO;
    private ElementoOrdineDAO elementoOrdineDAO;

    @Override
    public void init() throws ServletException {
        ordineDAO = new OrderDAO();
        prodottoDAO = new ProductDAO();
        elementoOrdineDAO = new ElementoOrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/view/utenteInterface/loginForm.jsp");
            return;
        }

        String emailCliente = user.getEmail();
        List<Order> ordini = ordineDAO.getOrdersByEmail(emailCliente);
        request.setAttribute("ordini", ordini);

        for (Order ordine : ordini) {
            List<ElementoOrdine> dettagliOrdine = elementoOrdineDAO.getOrderDetailsByOrderId(ordine.getNumeroOrdine());
            for (ElementoOrdine dettaglio : dettagliOrdine) {
                Product prodotto = null;
                try {
                    prodotto = prodottoDAO.getProductById(dettaglio.getIdProdotto());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                request.setAttribute("prodotto_" + dettaglio.getIdProdotto(), prodotto);
            }
        }

        request.getRequestDispatcher("view/ordiniInterface/listaOrdiniView.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
