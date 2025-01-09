import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import supporto.GestisciRichiestaSupportoControl;
import supporto.StatoSupporto;
import supporto.SupportRequest;
import supporto.SupportRequestDAO;
import util.DataSource;

public class TC10 {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private SupportRequestDAO supportRequestDAO;
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;

    @Mock
    private RequestDispatcher requestDispatcher;
    
    private GestisciRichiestaSupportoControl servlet;
    
    @Before
    public void setUp() throws ServletException, SQLException {
        MockitoAnnotations.openMocks(this);
        servlet = new GestisciRichiestaSupportoControl();
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        servlet.init();
        servlet.supportRequestDAO = supportRequestDAO;
    }

    @Test
    public void testDoGet() throws ServletException, IOException, SQLException {
        List<SupportRequest> richiesteMock = Arrays.asList(
                new SupportRequest(1, "Problema login", new Date(System.currentTimeMillis()), "14:00", StatoSupporto.IN_LAVORAZIONE, 
                                   "", "", 1)
        );
        
        when(supportRequestDAO.getAllSupportRequests()).thenReturn(richiesteMock);
        when(request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp")).thenReturn(requestDispatcher);
        
        servlet.doGet(request, response);
        
        verify(request).setAttribute("richieste", richiesteMock);
        verify(request).getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp");
    }

    @Test
    public void testDoGetSQLException() throws ServletException, IOException, SQLException {
        when(supportRequestDAO.getAllSupportRequests()).thenThrow(new SQLException("Errore database"));
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        servlet.doGet(request, response);

        verify(request).setAttribute("errorMessage", "Errore nel recupero delle richieste di supporto.");
        verify(request).getRequestDispatcher("view/error/messaggioErrore.jsp");
    }

    @Test
    public void testDoPost_RichiediInformazioni_Valid() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("richiediInformazioni");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("informazioniAggiuntive")).thenReturn("Informazioni");

        SupportRequest richiestaMock = new SupportRequest(1, "Problema login", new Date(System.currentTimeMillis()), "14:00", 
                                                          StatoSupporto.IN_LAVORAZIONE, "", "", 1);
        when(supportRequestDAO.getSupportRequestById(1)).thenReturn(richiestaMock);
        when(request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp")).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(supportRequestDAO).updateSupportRequest(richiestaMock);
        verify(request).getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp");
    }

    @Test
    public void testDoPost_RichiediInformazioni_Invalid() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("richiediInformazioni");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("informazioniAggiuntive")).thenReturn("");

        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "ID della richiesta o informazioni aggiuntive non valide.");
        verify(request).getRequestDispatcher("view/error/messaggioErrore.jsp");
    }

    @Test
    public void testDoPost_AggiornaStato_Valid() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("aggiornaStato");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("nuovoStato")).thenReturn("In lavorazione");
        
        SupportRequest richiestaMock = new SupportRequest(1, "Problema login", new Date(System.currentTimeMillis()), "14:00", 
                                                          StatoSupporto.IN_LAVORAZIONE, "", "", 1);
        when(supportRequestDAO.getSupportRequestById(1)).thenReturn(richiestaMock);
        when(request.getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp")).thenReturn(requestDispatcher);
        
        servlet.doPost(request, response);

        verify(supportRequestDAO).updateSupportRequest(richiestaMock);
        verify(request).getRequestDispatcher("view/supportoInterface/catalogoRichiesteSupporto.jsp");
    }

    @Test
    public void testDoPost_AggiornaStato_Invalid() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("aggiornaStato");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("nuovoStato")).thenReturn("");

        SupportRequest richiestaMock = new SupportRequest(1, "Problema login", new Date(System.currentTimeMillis()), "14:00", 
                                                          StatoSupporto.IN_LAVORAZIONE, "", "", 1);
        when(supportRequestDAO.getSupportRequestById(1)).thenReturn(richiestaMock);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "ID richiesta o stato non valido.");
        verify(request).getRequestDispatcher("view/error/messaggioErrore.jsp");
    }

    @Test
    public void testDoPost_RichiediInformazioni_RichiestaNonEsistente() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("richiediInformazioni");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("informazioniAggiuntive")).thenReturn("Informazioni");

        when(supportRequestDAO.getSupportRequestById(1)).thenReturn(null);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "La richiesta di supporto non esiste.");
        verify(request).getRequestDispatcher("view/error/messaggioErrore.jsp");
    }
    
    @Test
    public void testDoPost_AggiornaStato_RichiestaNonEsistente() throws ServletException, IOException, SQLException {
    	String idRichiesta = "1";
        
    	when(request.getParameter("azione")).thenReturn("aggiornaStato");
    	when(request.getParameter("richiestaId")).thenReturn(idRichiesta);
    	when(request.getParameter("nuovoStato")).thenReturn("In lavorazione");
        when(supportRequestDAO.getSupportRequestById(Integer.parseInt(idRichiesta))).thenReturn(null);
        when(request.getRequestDispatcher("view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);

        servlet.doPost(request, response);

        verify(request).setAttribute("errorMessage", "La richiesta non esiste.");
        verify(request).getRequestDispatcher("view/error/messaggioErrore.jsp");
    }
}
