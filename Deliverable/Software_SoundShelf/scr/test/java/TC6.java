import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import ordini.ElementoOrdine;
import ordini.ElementoOrdineDAO;
import prodotti.ProductDAO;
import rimborsi.InviaRichiestaRimborsoControl;
import rimborsi.RefoundRequest;
import rimborsi.RefoundRequestDAO;
import utente.UtenteRegistrato;
import utente.UtenteRegistratoDAO;
import util.DataSource;

import static org.mockito.Mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TC6 {

    private InviaRichiestaRimborsoControl servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private PrintWriter writer;
    
    @Mock
    private ElementoOrdineDAO elementoOrdineDAO;
    @Mock
    private RefoundRequestDAO refoundRequestDAO;
    @Mock
    private UtenteRegistratoDAO utenteDAO;
    @Mock
    private ProductDAO productDAO;
    @Mock
    private UtenteRegistrato user;
    @Mock
    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        servlet = new InviaRichiestaRimborsoControl();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        writer = mock(PrintWriter.class);

        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);
        
        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");

        servlet.productDAO = productDAO;
        servlet.elementoOrdineDAO = elementoOrdineDAO;
        servlet.refoundRequestDAO = refoundRequestDAO;
        servlet.utenteDAO = utenteDAO;
    }

    @Test
    public void testDoGet_NotLoggedIn() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }

    @Test
    public void testDoGet_InvalidDetailCode() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("detailCode")).thenReturn("1000");
        
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Dettagli prodotto non trovati."));
        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }

    @Test
    public void testDoGet_DetailNotFound() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("detailCode")).thenReturn("123");

        when(elementoOrdineDAO.getOrderDetailsById(123)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Dettagli prodotto non trovati."));
        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }

    @Test
    public void testDoGet_Success() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("detailCode")).thenReturn("123");

        ElementoOrdine mockDetail = mock(ElementoOrdine.class);
        when(elementoOrdineDAO.getOrderDetailsById(123)).thenReturn(mockDetail);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("orderDetail"), eq(mockDetail));
        verify(request.getRequestDispatcher("/view/rimborsiInterface/richiestaRimborsoForm.jsp")).forward(request, response);
    }

    @Test
    public void testDoPost_NotLoggedIn() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), anyString());
        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }

    @Test
    public void testDoPost_InvalidParameters() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("orderDetailID")).thenReturn(null);
        when(request.getParameter("reason")).thenReturn(null);
        when(request.getParameter("iban")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Tutti i campi sono obbligatori."));
        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }

    @Test
    public void testDoPost_Success() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("orderDetailID")).thenReturn("123");
        when(request.getParameter("reason")).thenReturn("Motivo");
        when(request.getParameter("iban")).thenReturn("IT60X0542811101000000123456");

        ElementoOrdine mockProduct = mock(ElementoOrdine.class);
        when(elementoOrdineDAO.getOrderDetailsById(123)).thenReturn(mockProduct);

        RefoundRequest mockRefundRequest = mock(RefoundRequest.class);
        doNothing().when(refoundRequestDAO).saveRichiestaRimborso(mockRefundRequest);

        servlet.doPost(request, response);
        verify(request.getRequestDispatcher("listaOrdiniUtente")).forward(request, response);
    }

    @Test
    public void testDoPost_SaveRequestError() throws Exception {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("orderDetailID")).thenReturn("123");
        when(request.getParameter("reason")).thenReturn("Motivo");
        when(request.getParameter("iban")).thenReturn("IT60X0542811101000000123456");

        ElementoOrdine mockProduct = mock(ElementoOrdine.class);
        when(elementoOrdineDAO.getOrderDetailsById(123)).thenReturn(mockProduct);

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        
        when(dataSource.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(any(String.class))).thenReturn(mockStatement);
        
        doThrow(new SQLException("Errore database")).when(mockStatement).executeUpdate();

        RefoundRequest mockRefundRequest = mock(RefoundRequest.class);
        doThrow(new SQLException("Errore nel database")).when(refoundRequestDAO).saveRichiestaRimborso(any(RefoundRequest.class));

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errorMessage"), eq("Errore nel salvataggio della richiesta di rimborso."));

        verify(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).forward(request, response);
    }


}
