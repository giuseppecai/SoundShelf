import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import utente.LoginControl;
import utente.Ruolo;
import utente.UtenteRegistrato;
import utente.UtenteRegistratoDAO;
import util.DataSource;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TC1 {

    @Mock
    private UtenteRegistratoDAO userDAO;

    @InjectMocks
    private LoginControl loginControl;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

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
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        loginControl = new LoginControl();
        loginControl.setUserDAO(userDAO);
        loginControl.init();
    }

    @Test
    public void testLoginSuccess() throws ServletException, IOException {
        String email = "mario.rossi@example.com";
        String password = "password123";
        String hashedPassword = hashPassword(password);
        UtenteRegistrato expectedUser = new UtenteRegistrato(email, hashedPassword, "Mario", "Rossi", "Via Roma 10", "1234567890", Ruolo.UTENTE);

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        lenient().when(userDAO.authenticate(eq(email), eq(hashedPassword))).thenReturn(expectedUser);
        when(request.getSession()).thenReturn(session);

        loginControl.doPost(request, response);

        verify(userDAO).authenticate(eq(email), eq(hashedPassword));
        verify(session).setAttribute(eq("user"), eq(expectedUser));
        verify(response).sendRedirect(request.getContextPath() + "/home");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginWrongPassword() throws ServletException, IOException {
        String email = "mario.rossi@example.com";
        String password = "cagnolini123";
        String hashedPassword = "differenteHash";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        loginControl.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Email o password non validi.");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testLoginEmailNotFound() throws ServletException, IOException {
        String email = "mattiarossi@gmail.com";
        String password = "cagnolini123";
        String hashedPassword = "differenteHash";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("password")).thenReturn(password);
        lenient().when(userDAO.authenticate(eq(email), eq(hashedPassword))).thenReturn(null);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        loginControl.doPost(request, response);

        verify(request).setAttribute("errorMessage", "Email o password non validi.");
        verify(requestDispatcher).forward(request, response);
    }
}
