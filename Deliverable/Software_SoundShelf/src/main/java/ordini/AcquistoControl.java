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
import java.util.Calendar;

@WebServlet("/acquistoControl")
public class AcquistoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public OrderDAO orderDAO;
    public ElementoOrdineDAO elementoOrdineDAO;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        elementoOrdineDAO = new ElementoOrdineDAO();
        productDAO = new ProductDAO();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Il carrello è vuoto.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        double totalPrice = cart.getTotalPrice();
        request.setAttribute("totalPrice", totalPrice);

        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");
        if (user != null) {
            request.setAttribute("savedAddress", user.getIndirizzo());
        } else {
            request.setAttribute("errorMessage", "Per procedere con l'acquisto è necessario essere autenticati.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        request.getRequestDispatcher("view/ordiniInterface/checkout.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (cart == null || cart.isEmpty()) {
            request.setAttribute("errorMessage", "Il carrello è vuoto.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        if (user == null) {
            request.setAttribute("errorMessage", "Per procedere con l'acquisto è necessario essere autenticati.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        String useNewAddress = request.getParameter("useNewAddress");
        String shippingAddress;

        if ("on".equals(useNewAddress)) {
            String street = request.getParameter("via");
            String civicNumber = request.getParameter("numeroCivico");
            String postalCode = request.getParameter("cap");
            String city = request.getParameter("citta");
            String province = request.getParameter("provincia");

            if (street == null || civicNumber == null || postalCode == null || city == null || province == null ||
                street.trim().isEmpty() || civicNumber.trim().isEmpty() || postalCode.trim().isEmpty() ||
                city.trim().isEmpty() || province.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Tutti i campi del nuovo indirizzo sono obbligatori.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            shippingAddress = street + " , " + civicNumber + " , " + postalCode + " , " + city + " , " + province;
        } else {
            shippingAddress = user.getIndirizzo();
        }

        if (shippingAddress.trim().isEmpty() || shippingAddress.length() < 10) {
            request.setAttribute("errorMessage", "L'indirizzo di spedizione non è valido. Assicurati che contenga almeno 10 caratteri.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        Order order = new Order();
        order.setEmailCliente(user.getEmail());
        order.setPrezzoTotale(cart.getTotalPrice());
        order.setDataOrdine(new java.sql.Date(System.currentTimeMillis()));
        order.setIndirizzoSpedizione(shippingAddress);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        order.setDataConsegna(new java.sql.Date(calendar.getTimeInMillis()));
        order.setStato(StatoOrdine.ATTESA_PAGAMENTO);

        try {
            for (CartItem item : cart.getItems()) {
                Product product = item.getProduct();
                int quantity = item.getQuantity();

                if (product.getAvailability() <= 0 || product.getAvailability() < quantity) {
                    request.setAttribute("errorMessage", "Il prodotto " + product.getName() + " non è disponibile nella quantità richiesta.");
                    request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                    return;
                }
            }

            int numeroOrdine = orderDAO.addOrder(order);

            for (CartItem item : cart.getItems()) {
                Product product = item.getProduct();
                int quantity = item.getQuantity();

                ElementoOrdine elementoOrdine = new ElementoOrdine();
                elementoOrdine.setIdOrdine(numeroOrdine);
                elementoOrdine.setIdProdotto(product.getProductCode());
                elementoOrdine.setQuantita(quantity);
                elementoOrdine.setPrezzoUnitario(product.getSalePrice());
                elementoOrdineDAO.addOrderDetail(elementoOrdine);

                product.setAvailability(product.getAvailability() - quantity);
                productDAO.updateProduct(product);
            }

            cart.clear();
            session.setAttribute("cart", cart);
            session.setAttribute("order", order);
            response.sendRedirect(request.getContextPath() + "/view/ordiniInterface/orderConfirmation.jsp");

        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore nella creazione dell'ordine.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}
