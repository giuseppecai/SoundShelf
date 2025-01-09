import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ordini.AcquistoControl;
import ordini.Cart;
import ordini.CartItem;
import ordini.ElementoOrdineDAO;
import ordini.Order;
import ordini.OrderDAO;
import prodotti.Product;
import prodotti.ProductDAO;
import utente.UtenteRegistrato;
import util.DataSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TC2 {

    @InjectMocks
    private AcquistoControl acquistoControl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Cart cart;

    @Mock
    private UtenteRegistrato user;

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private ElementoOrdineDAO elementoOrdineDAO;

    @Mock
    private Product product;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @BeforeEach
    public void setUp() throws ServletException, SQLException {
        lenient().when(request.getSession()).thenReturn(session);
        lenient().when(session.getAttribute("cart")).thenReturn(cart);
        lenient().when(session.getAttribute("user")).thenReturn(user);
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        acquistoControl.orderDAO = orderDAO;
        acquistoControl.elementoOrdineDAO = elementoOrdineDAO;
        acquistoControl.productDAO = productDAO;
    }

    @Test
    public void testUnauthenticatedUser() throws ServletException, IOException {
        when(session.getAttribute("user")).thenReturn(null);
        when(cart.isEmpty()).thenReturn(false);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        acquistoControl.doGet(request, response);

        verify(request).setAttribute("errorMessage", "Per procedere con l'acquisto è necessario essere autenticati.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testAuthenticatedUserWithNewAddress() throws ServletException, IOException, SQLException {
        String useNewAddress = "on";
        double totalPrice = 100.0;

        when(cart.isEmpty()).thenReturn(false);
        when(cart.getTotalPrice()).thenReturn(totalPrice);
        when(request.getParameter("useNewAddress")).thenReturn(useNewAddress);
        when(request.getParameter("via")).thenReturn("Pellicano");
        when(request.getParameter("numeroCivico")).thenReturn("1");
        when(request.getParameter("cap")).thenReturn("11111");
        when(request.getParameter("citta")).thenReturn("Ancona");
        when(request.getParameter("provincia")).thenReturn("SA");
        
        when(orderDAO.addOrder(any(Order.class))).thenReturn(1);
        when(product.getAvailability()).thenReturn(10);

        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(product, 1));
        when(cart.getItems()).thenReturn(items);

        acquistoControl.doPost(request, response);

        verify(response).sendRedirect(anyString());
        verify(orderDAO).addOrder(any(Order.class));
    }

    @Test
    public void testProductNotAvailable() throws ServletException, IOException, SQLException {
        when(cart.isEmpty()).thenReturn(false);
        when(cart.getTotalPrice()).thenReturn(100.0);
        when(request.getParameter("useNewAddress")).thenReturn("off");

        when(product.getAvailability()).thenReturn(0); // Prodotto non disponibile
        when(product.getName()).thenReturn("Prodotto Test");

        List<CartItem> items = new ArrayList<>();
        items.add(new CartItem(product, 1));
        when(cart.getItems()).thenReturn(items);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);
        
        when(user.getIndirizzo()).thenReturn("Via Brombei, 17, Napoli, NA");
        acquistoControl.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Il prodotto Prodotto Test non è disponibile nella quantità richiesta.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testShippingAddressValidation() throws ServletException, IOException {
        when(cart.isEmpty()).thenReturn(false);
        when(request.getParameter("useNewAddress")).thenReturn("off"); // Indirizzo non valido
        when(user.getIndirizzo()).thenReturn("Via");

        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        acquistoControl.doPost(request, response);

        verify(request).setAttribute("errorMessage", "L'indirizzo di spedizione non è valido. Assicurati che contenga almeno 10 caratteri.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testEmptyCart() throws ServletException, IOException {
        when(cart.isEmpty()).thenReturn(true); // Carrello vuoto
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        acquistoControl.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Il carrello è vuoto.");
        verify(requestDispatcher).forward(request, response);
    }

}
