package rimborsi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import utente.UtenteRegistrato;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/richiestaRimborsoControl")
public class RichiestaRimborsoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RefoundRequestDAO refoundRequestDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        refoundRequestDAO = new RefoundRequestDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user != null) {
            try {
                List<RefoundRequest> refoundRequests = refoundRequestDAO.getRichiesteRimborsoPerUtente(user.getEmail());
                request.setAttribute("refoundRequests", refoundRequests);
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Errore generico.");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
            }
        } else {
        	request.setAttribute("errorMessage", "Devi essere loggato per visualizzare le richieste di rimborso.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }

        request.getRequestDispatcher("view/rimborsiInterface/richiestaRimborsoView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
