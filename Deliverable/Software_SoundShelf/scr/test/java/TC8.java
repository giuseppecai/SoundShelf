import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import prodotti.Product;
import prodotti.ProductDAO;
import prodotti.RimuoviProdottoControl;

class TC8 {

    private RimuoviProdottoControl rimuoviProdottoControl;

    @Mock
    private ProductDAO productDAO;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        rimuoviProdottoControl = new RimuoviProdottoControl();
        rimuoviProdottoControl.productDAO = productDAO;  // Set the mocked DAO directly

        // Mock the RequestDispatcher to avoid NullPointerException
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    void testProductExists() throws ServletException, IOException, SQLException {
        // Given
        int productId = 3221;
        Product mockProduct = new Product();
        mockProduct.setProductCode(productId);
        
        when(request.getParameter("productId")).thenReturn(String.valueOf(productId));
        when(productDAO.getProductById(productId)).thenReturn(mockProduct);
        
        // When
        rimuoviProdottoControl.doPost(request, response);
        
        // Then
        verify(productDAO).deleteProduct(productId);  // Verifying that the delete method is called
        verify(response).sendRedirect("gestisciCatalogoProdottiControl"); // Verifying the redirect to catalog page
    }

    @Test
    void testProductNotFound() throws ServletException, IOException, SQLException {
        // Given
        int productId = 1;  // ID of a non-existing product
        when(request.getParameter("productId")).thenReturn(String.valueOf(productId));
        when(productDAO.getProductById(productId)).thenReturn(null);
        
        // When
        rimuoviProdottoControl.doPost(request, response);
        
        // Then
        verify(request).setAttribute("errorMessage", "Product not found");
        verify(requestDispatcher).forward(request, response); // Verifying the forward to error page
    }

    @Test
    void testSQLExceptionOnDelete() throws ServletException, IOException, SQLException {
        // Given
        int productId = 3221;
        Product mockProduct = new Product();
        mockProduct.setProductCode(productId);
        
        when(request.getParameter("productId")).thenReturn(String.valueOf(productId));
        when(productDAO.getProductById(productId)).thenReturn(mockProduct);
        doThrow(new SQLException("Database error")).when(productDAO).deleteProduct(productId);
        
        // When
        rimuoviProdottoControl.doPost(request, response);
        
        // Then
        verify(request).setAttribute("errorMessage", "Error removing product from the database");
        verify(requestDispatcher).forward(request, response); // Verifying the forward to error page
    }

    @Test
    void testProductCodeMissing() throws ServletException, IOException {
        // Given
        when(request.getParameter("productId")).thenReturn(null);
        
        // When
        rimuoviProdottoControl.doPost(request, response);
        
        // Then
        verify(request).setAttribute("errorMessage", "Product code is required");
        verify(requestDispatcher).forward(request, response); // Verifying the forward to error page
    }

    @Test
    void testInvalidProductCodeFormat() throws ServletException, IOException {
        // Given
        when(request.getParameter("productId")).thenReturn("invalidCode");
        
        // When
        rimuoviProdottoControl.doPost(request, response);
        
        // Then
        verify(request).setAttribute("errorMessage", "Invalid product code format");
        verify(requestDispatcher).forward(request, response); // Verifying the forward to error page
    }
}
