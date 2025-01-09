import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import prodotti.ProductDAO;
import recensione.RecensioniControl;
import utente.UtenteRegistrato;
import util.DataSource;
import ordini.Cart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class TC4 {

    @InjectMocks
    private RecensioniControl recensioneControl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Cart cart;

    @Mock
    private UtenteRegistrato user;
    
    @Mock
    private ProductDAO productDAO;

    @Mock
    private HttpSession session;
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    public void setUp() throws ServletException, SQLException {
        lenient().when(request.getSession()).thenReturn(session);
        lenient().when(session.getAttribute("cart")).thenReturn(cart);
        lenient().when(session.getAttribute("user")).thenReturn(user);
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher); // Mock getRequestDispatcher
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        recensioneControl.init();
        recensioneControl.productDAO = productDAO;
    }

    @Test
    public void testValidReview() throws Exception {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rating")).thenReturn("5");
        when(request.getParameter("comment")).thenReturn("Stupendo!");

        recensioneControl.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testMissingVoto() throws Exception {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rating")).thenReturn(null);
        when(request.getParameter("comment")).thenReturn("Stupendo!");

        recensioneControl.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Compilare tutti i campi obbligatori"));
        verify(requestDispatcher).forward(request, response);  // Verifica che il forward venga invocato
    }

    @Test
    public void testInvalidVoto() throws Exception {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rating")).thenReturn("8");
        when(request.getParameter("comment")).thenReturn("Stupendo!");

        recensioneControl.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Voto non valido. Inserire un valore tra 0 e 5"));
        verify(requestDispatcher).forward(request, response);  // Verifica che il forward venga invocato
    }

    @Test
    public void testMissingDescrizione() throws Exception {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rating")).thenReturn("5");
        when(request.getParameter("comment")).thenReturn(null);

        recensioneControl.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Compilare tutti i campi obbligatori"));
        verify(requestDispatcher).forward(request, response);  // Verifica che il forward venga invocato
    }

    @Test
    public void testInvalidDescrizione() throws Exception {
        when(request.getParameter("productId")).thenReturn("1");
        when(request.getParameter("rating")).thenReturn("5");
        when(request.getParameter("comment")).thenReturn("-");
        
        recensioneControl.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Descrizione non valida. Inserire una descrizione adeguata"));
        verify(requestDispatcher).forward(request, response);  // Verifica che il forward venga invocato
    }
}
