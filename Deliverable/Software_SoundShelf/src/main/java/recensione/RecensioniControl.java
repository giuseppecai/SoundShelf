package recensione;

import prodotti.Product;
import prodotti.ProductDAO;
import utente.UtenteRegistrato;
import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/addReview")
public class RecensioniControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewDAO reviewDAO;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        reviewDAO = new ReviewDAO();
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("view/utenteInterface/loginForm.jsp");
            return;
        }
        
        int productId = Integer.parseInt(request.getParameter("productCode"));

        Product purchasedProduct = null;;
		try {
			purchasedProduct = productDAO.getProductById(productId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        request.setAttribute("purchasedProduct", purchasedProduct);
        request.getRequestDispatcher("view/recensioneInterface/recensioneForm.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("view/utenteInterface/loginForm.jsp");
            return;
        }

        try {
            int productId = Integer.parseInt(request.getParameter("productId"));
            String ratingStr = request.getParameter("rating");
            String comment = InputSanitizer.sanitize(request.getParameter("comment"));

            if (ratingStr == null || comment == null) {
                request.setAttribute("errorMessage", "Compilare tutti i campi obbligatori");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            int rating = Integer.parseInt(ratingStr);
            if (rating < 0 || rating > 5) {
                request.setAttribute("errorMessage", "Voto non valido. Inserire un valore tra 0 e 5");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            if (comment.isEmpty() || comment.length() < 3) {
                request.setAttribute("errorMessage", "Descrizione non valida. Inserire una descrizione adeguata");
                request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                return;
            }

            Review review = new Review();
            review.setIdProdotto(productId);
            review.setEmailCliente(user.getEmail());
            review.setVoto(rating);
            review.setDescrizione(comment);
            review.setDataRecensione(new Date(System.currentTimeMillis()));

            reviewDAO.saveReview(review);
            request.getRequestDispatcher("listaOrdiniUtente").forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Errore nei parametri inviati");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Errore nel salvataggio della recensione");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

}
