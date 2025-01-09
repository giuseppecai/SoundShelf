package prodotti;

import org.json.JSONArray;
import org.json.JSONObject;
import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/searchProducts")
public class CercaProdottiControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = InputSanitizer.sanitize(request.getParameter("name"));
        String genreParam = InputSanitizer.sanitize(request.getParameter("genre"));
        String artistParam = InputSanitizer.sanitize(request.getParameter("artist"));

        System.out.println("Nome: " + name);
        System.out.println("Genere: " + genreParam);
        System.out.println("Artista: " + artistParam);

        List<String> genres = (genreParam != null && !genreParam.isEmpty())
                ? List.of(genreParam.split(","))
                : null;
        List<String> artists = (artistParam != null && !artistParam.isEmpty())
                ? List.of(artistParam.split(","))
                : null;
        
        try {
            List<Product> products = productDAO.searchProducts(name, genres, artists);
            System.out.println(products);
            System.out.println("Prodotti trovati: " + products.size());

            JSONArray jsonArray = new JSONArray();
            for (Product product : products) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("productCode", product.getProductCode());
                jsonObject.put("name", product.getName());
                jsonObject.put("description", product.getDescription());
                jsonObject.put("artists", product.getArtists());
                jsonObject.put("genres", product.getGenres());
                jsonObject.put("salePrice", product.getSalePrice());
                jsonObject.put("originalPrice", product.getOriginalPrice());
                jsonObject.put("image", product.getImage());
                jsonObject.put("availability", product.getAvailability());
                jsonObject.put("releaseDate", product.getReleaseDate());
                jsonArray.put(jsonObject);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonArray.toString());

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"Errore nella ricerca dei prodotti.\"}");
        }
    }
}
