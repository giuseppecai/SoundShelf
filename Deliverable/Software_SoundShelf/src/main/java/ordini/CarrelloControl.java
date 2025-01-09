package ordini;

import prodotti.Product;
import prodotti.ProductDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/carrelloControl")
public class CarrelloControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if (action != null && !action.equals("clear")) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            Product product = null;
            try {
                product = productDAO.getProductById(productId);
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Errore nel recupero del prodotto.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            switch (action) {
                case "add":
                    cart.addProduct(product);
                    break;
                case "remove":
                    cart.removeProduct(product);
                    break;
                case "update":
                    int quantity = -1;
                    try {
                        quantity = Integer.parseInt(request.getParameter("quantity"));
                        if (quantity <= 0) {
                            throw new NumberFormatException(); 
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "Quantità non valida");
                        request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                        return;
                    }
                    cart.updateQuantity(product, quantity);
                    break;
            }
        } else if(action != null && action.equals("clear")) {
            cart.clear();
        }

        response.sendRedirect("view/ordiniInterface/carrelloView.jsp");
    }

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
