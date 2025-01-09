package recensione;

import utente.UtenteRegistrato;
import utente.UtenteRegistratoDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/rimuoviRecensioneControl")
public class RimuoviRecensioneControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ReviewDAO reviewDAO;

    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        String reviewIdParam = request.getParameter("reviewId");
        if (reviewIdParam == null || reviewIdParam.isEmpty()) {
            request.setAttribute("errorMessage", "Il codice della recensione è obbligatorio");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        int reviewId = Integer.parseInt(reviewIdParam);
        if(reviewId < 0) {
            	request.setAttribute("errorMessage", "Il codice della recensione non è valido");
                request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
        }

        Review review = null;
        try {
            review = reviewDAO.getReviewById(reviewId);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore durante la rimozione della recensione");
            request.getRequestDispatcher("/messaggioErrore").forward(request, response);
            return;
        }

        if (review == null) {
            request.setAttribute("errorMessage", "Recensione non presente.");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        try {
            reviewDAO.deleteReview(reviewId);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore durante la rimozione della recensione");
            request.getRequestDispatcher("/view/error/messaggioErrore.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/gestisciRecensioniControl");
    }
}
