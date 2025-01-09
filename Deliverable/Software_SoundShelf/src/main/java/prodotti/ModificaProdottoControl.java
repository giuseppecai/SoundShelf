package prodotti;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ModificaProdottoControl")
public class ModificaProdottoControl extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productCodeStr = request.getParameter("productId");
        if (productCodeStr != null) {
            try {
                int productCode = Integer.parseInt(productCodeStr);
                Product product = productDAO.getProductById(productCode);
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("/view/prodottiInterface/modificaProdottoForm.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Product not found");
                    request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
                }
            } catch (SQLException | NumberFormatException e) {
                request.setAttribute("errorMessage", "Error fetching product");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("errorMessage", "Product code is required");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int productCode = Integer.parseInt(request.getParameter("productCode"));
            String name = request.getParameter("name");
            String releaseDate = request.getParameter("releaseDate");
            String description = request.getParameter("description");
            int availability = Integer.parseInt(request.getParameter("availability"));
            double salePrice = Double.parseDouble(request.getParameter("salePrice"));
            double originalPrice = Double.parseDouble(request.getParameter("originalPrice"));
            String supportedDevice = request.getParameter("supportedDevice");
            String image = request.getParameter("image");

            Product product = null;
            try {
                product = productDAO.getProductById(productCode);
            } catch (SQLException e) {
                request.setAttribute("errorMessage", "Error accessing product database");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }

            if (product != null) {
                product.setName(name);
                product.setReleaseDate(releaseDate);
                product.setDescription(description);
                product.setAvailability(availability);
                product.setSalePrice(salePrice);
                product.setOriginalPrice(originalPrice);
                product.setSupportedDevice(supportedDevice);
                product.setImage(image);

                try {
                    productDAO.updateProduct(product);
                } catch (SQLException e) {
                    request.setAttribute("errorMessage", "Error updating product");
                    request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
                    return;
                }
                response.sendRedirect(request.getContextPath() + "/gestisciCatalogoProdottiControl");
            } else {
                request.setAttribute("errorMessage", "Product not found");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Error updating product");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

}
