package prodotti;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/rimuoviProdottoControl")
public class RimuoviProdottoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCodeStr = request.getParameter("productId");

        if (productCodeStr != null) {
            try {
                int productCode = Integer.parseInt(productCodeStr);
                Product product = null;
                try {
                    product = productDAO.getProductById(productCode);
                } catch (SQLException e) {
                    request.setAttribute("errorMessage", "Error accessing product database");
                    request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
                }

                if (product != null) {
                    try {
                        productDAO.deleteProduct(productCode);
                    } catch (SQLException e) {
                        request.setAttribute("errorMessage", "Error removing product from the database");
                        request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
                        return;
                    }
                    response.sendRedirect("gestisciCatalogoProdottiControl");
                } else {
                    request.setAttribute("errorMessage", "Product not found");
                    request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid product code format");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Product code is required");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}
