import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ordini.Cart;
import ordini.CartItem;
import ordini.CarrelloControl;
import prodotti.Product;
import prodotti.ProductDAO;
import util.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;

public class TC3 {

    private CarrelloControl carrelloControl;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpSession session;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem;
    @Mock
    private Product product;
    @Mock
    private ProductDAO productDAO;
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws ServletException, SQLException {
        MockitoAnnotations.openMocks(this);
        carrelloControl = new CarrelloControl();
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        carrelloControl.init();
        carrelloControl.productDAO = productDAO;
    }

    @Test
    public void testTC6_2_InvalidQuantityUpdate() throws ServletException, IOException, SQLException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("-1"); // Quantità non valida
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);
        when(productDAO.getProductById(1)).thenReturn(product);

        carrelloControl.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Quantità non valida"));
        verify(requestDispatcher).forward(request, response);
        verifyNoMoreInteractions(response);
    }

    @Test
    public void testTC6_1_ValidQuantityUpdate() throws ServletException, IOException, SQLException {
        when(request.getParameter("action")).thenReturn("update");
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("quantity")).thenReturn("3"); // Quantità valida
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(cart.getItems()).thenReturn(Collections.singletonList(cartItem));
        when(cartItem.getProduct()).thenReturn(product);
        when(product.getProductCode()).thenReturn(1);
        when(productDAO.getProductById(1)).thenReturn(product);

        carrelloControl.doPost(request, response);

        verify(cart).updateQuantity(eq(product), eq(3));
        verify(response).sendRedirect("view/ordiniInterface/carrelloView.jsp");
        verifyNoMoreInteractions(response);
    }

    @Test
    public void testAddProductToCart() throws ServletException, IOException, SQLException {
        when(request.getParameter("action")).thenReturn("add");
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productDAO.getProductById(1)).thenReturn(product);

        carrelloControl.doPost(request, response);

        verify(cart).addProduct(eq(product));
        verify(response).sendRedirect("view/ordiniInterface/carrelloView.jsp");
    }

    @Test
    public void testRemoveProductFromCart() throws ServletException, IOException, SQLException {
        when(request.getParameter("action")).thenReturn("remove");
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("cart")).thenReturn(cart);
        when(productDAO.getProductById(1)).thenReturn(product);

        carrelloControl.doPost(request, response);

        verify(cart).removeProduct(eq(product));
        verify(response).sendRedirect("view/ordiniInterface/carrelloView.jsp");
    }
}
