package control;

import prodotti.Product;
import prodotti.ProductDAO;
import recensione.Review;
import recensione.ReviewDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Product> products = null;
        try {
            products = productDAO.getAllProductsHome();
        } catch (SQLException e) {
            request.setAttribute("message", "Errore nel recupero dei prodotti.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }  
        request.setAttribute("products", products);
        request.getRequestDispatcher("view/home/homeView.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
