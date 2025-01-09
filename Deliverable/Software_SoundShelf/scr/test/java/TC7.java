import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import prodotti.ModificaProdottoControl;
import prodotti.Product;
import prodotti.ProductDAO;
import prodotti.Artist;
import prodotti.Genre;

public class TC7 {

    private ModificaProdottoControl modificaProdottoControl;

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getRequestDispatcher("/view/prodottiInterface/modificaProdottoForm.jsp")).thenReturn(requestDispatcher);
        when(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);
        modificaProdottoControl = new ModificaProdottoControl();
        modificaProdottoControl.productDAO = productDAO;
    }

    @Test
    void testDoGetProductFound() throws ServletException, IOException, SQLException {
        // Arrange
        int productCode = 1;
        Artist artist = new Artist("Michael", "Jackson", "Michael Jackson");
        Genre genre = new Genre("Pop");
        List<Artist> artists = Arrays.asList(artist);
        List<Genre> genres = Arrays.asList(genre);
        
        Product product = new Product(productCode, "Thriller", artists, "30/11/1982", "Album iconico", 
                                      100, 15.99, 19.99, "CD", genres, "thriller.jpg", false);

        when(request.getParameter("productId")).thenReturn(String.valueOf(productCode));
        when(productDAO.getProductById(productCode)).thenReturn(product);

        // Act
        modificaProdottoControl.doGet(request, response);

        // Assert
        verify(request).setAttribute("product", product);
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGetProductNotFound() throws ServletException, IOException, SQLException {
        // Arrange
        int productCode = 1;
        when(request.getParameter("productId")).thenReturn(String.valueOf(productCode));
        when(productDAO.getProductById(productCode)).thenReturn(null);

        // Act
        modificaProdottoControl.doGet(request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Product not found");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoGetInvalidProductId() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("productId")).thenReturn(null);

        // Act
        modificaProdottoControl.doGet(request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Product code is required");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPostProductUpdated() throws ServletException, IOException, SQLException {
        // Arrange
        int productCode = 1;
        String name = "Thriller Updated";
        String releaseDate = "30/11/1983";
        String description = "Updated description";
        int availability = 150;
        double salePrice = 17.99;
        double originalPrice = 19.99;
        String supportedDevice = "Vinyl";
        String image = "thriller_updated.jpg";

        Artist artist = new Artist("Michael", "Jackson", "Michael Jackson");
        Genre genre = new Genre("Pop");
        List<Artist> artists = Arrays.asList(artist);
        List<Genre> genres = Arrays.asList(genre);

        Product product = new Product(productCode, "Thriller", artists, "30/11/1982", "Album iconico", 
                                      100, 15.99, 19.99, "CD", genres, "thriller.jpg", false);

        when(request.getParameter("productCode")).thenReturn(String.valueOf(productCode));
        when(request.getParameter("name")).thenReturn(name);
        when(request.getParameter("releaseDate")).thenReturn(releaseDate);
        when(request.getParameter("description")).thenReturn(description);
        when(request.getParameter("availability")).thenReturn(String.valueOf(availability));
        when(request.getParameter("salePrice")).thenReturn(String.valueOf(salePrice));
        when(request.getParameter("originalPrice")).thenReturn(String.valueOf(originalPrice));
        when(request.getParameter("supportedDevice")).thenReturn(supportedDevice);
        when(request.getParameter("image")).thenReturn(image);
        when(productDAO.getProductById(productCode)).thenReturn(product);

        // Act
        modificaProdottoControl.doPost(request, response);

        // Assert
        verify(productDAO).updateProduct(any(Product.class));
        verify(response).sendRedirect(request.getContextPath() + "/gestisciCatalogoProdottiControl");
    }

    @Test
    void testDoPostProductNotFound() throws ServletException, IOException, SQLException {
        // Arrange
        int productCode = 1;
        when(request.getParameter("productCode")).thenReturn(String.valueOf(productCode));
        when(productDAO.getProductById(productCode)).thenReturn(null);
        when(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        // Act
        modificaProdottoControl.doPost(request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Error updating product");
        verify(requestDispatcher).forward(request, response);
    }


    @Test
    void testDoPostSQLException() throws ServletException, IOException, SQLException {
        // Arrange
        int productCode = 1;
        when(request.getParameter("productCode")).thenReturn(String.valueOf(productCode));
        when(productDAO.getProductById(productCode)).thenThrow(new SQLException());

        // Act
        modificaProdottoControl.doPost(request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Error updating product");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    void testDoPostInvalidParameters() throws ServletException, IOException {
        // Arrange
        when(request.getParameter("productCode")).thenReturn("invalid");

        // Act
        modificaProdottoControl.doPost(request, response);

        // Assert
        verify(request).setAttribute("errorMessage", "Error updating product");
        verify(requestDispatcher).forward(request, response);
    }
}
