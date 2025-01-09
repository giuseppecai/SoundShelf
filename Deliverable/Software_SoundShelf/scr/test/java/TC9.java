import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import recensione.Review;
import recensione.ReviewDAO;
import recensione.RimuoviRecensioneControl;
import utente.UtenteRegistrato;
import util.DataSource;

class TC9 {

    private RimuoviRecensioneControl rimuoviRecensioneControl;

    @Mock
    private ReviewDAO reviewDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private UtenteRegistrato user;

    @Mock
    private Connection connection;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        rimuoviRecensioneControl = new RimuoviRecensioneControl();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getRequestDispatcher("/view/error/messaggioErrore.jsp")).thenReturn(requestDispatcher);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        rimuoviRecensioneControl.init();
        rimuoviRecensioneControl.reviewDAO = reviewDAO;
    }

    @Test
    void testReviewExists() throws ServletException, IOException, SQLException {
        int reviewId = 1;
        Review mockReview = new Review();
        mockReview.setId(reviewId);

        when(request.getParameter("reviewId")).thenReturn(String.valueOf(reviewId));
        when(reviewDAO.getReviewById(reviewId)).thenReturn(mockReview);

        rimuoviRecensioneControl.doPost(request, response);

        verify(reviewDAO, times(1)).deleteReview(reviewId);

        verify(response, times(1)).sendRedirect(anyString());
    }


    @Test
    void testReviewNotFound() throws ServletException, IOException, SQLException {
        int reviewId = 1;
        when(request.getParameter("reviewId")).thenReturn(String.valueOf(reviewId));
        when(reviewDAO.getReviewById(reviewId)).thenReturn(null);
        rimuoviRecensioneControl.doPost(request, response);
        verify(requestDispatcher).forward(request, response);  // Verifica che il forwarding venga invocato
    }

    @Test
    void testSQLExceptionOnDelete() throws ServletException, IOException, SQLException {
        int reviewId = 3221;
        Review mockReview = new Review();
        mockReview.setId(reviewId);
        when(request.getParameter("reviewId")).thenReturn(String.valueOf(reviewId));
        when(reviewDAO.getReviewById(reviewId)).thenReturn(mockReview);
        doThrow(new SQLException("Errore nel database")).when(reviewDAO).deleteReview(reviewId);
        rimuoviRecensioneControl.doPost(request, response);
        verify(requestDispatcher).forward(request, response);  // Verifica che il forwarding venga invocato
    }

    @Test
    void testReviewCodeMissing() throws ServletException, IOException {
        when(request.getParameter("reviewId")).thenReturn(null);
        rimuoviRecensioneControl.doPost(request, response);
        verify(requestDispatcher).forward(request, response);  // Verifica che il forwarding venga invocato
    }

    @Test
    void testInvalidReviewCodeFormat() throws ServletException, IOException {
        when(request.getParameter("reviewId")).thenReturn("");
        rimuoviRecensioneControl.doPost(request, response);
        verify(requestDispatcher, times(1)).forward(request, response);
    }
}
