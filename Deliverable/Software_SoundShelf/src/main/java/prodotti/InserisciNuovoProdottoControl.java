package prodotti;

import util.InputSanitizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/InserisciNuovoProdottoControl")
public class InserisciNuovoProdottoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = InputSanitizer.sanitize(request.getParameter("name"));
            String description = InputSanitizer.sanitize(request.getParameter("description"));
            double salePrice = Double.parseDouble(request.getParameter("salePrice"));
            double originalPrice = Double.parseDouble(request.getParameter("originalPrice"));
            int availability = Integer.parseInt(request.getParameter("availability"));
            String releaseDate = InputSanitizer.sanitize(request.getParameter("releaseDate"));
            String image = InputSanitizer.sanitize(request.getParameter("image"));
            String device = InputSanitizer.sanitize(request.getParameter("supportedDevice"));

            String rawArtists = request.getParameter("artists");
            List<Artist> artistList = new ArrayList<>();
            if (rawArtists != null && !rawArtists.trim().isEmpty()) {
                String[] artistNames = InputSanitizer.sanitize(rawArtists).split(",");
                for (String artistName : artistNames) {
                    artistName = artistName.trim();
                    Artist artist = productDAO.findArtistByName(artistName);
                    System.out.println(artist);
                    if (artist != null) {
                        artistList.add(artist);
                    } else {
                        request.setAttribute("errorMessage", "Artista " + artistName + " non trovato.");
                        request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                        return;
                    }
                }
            }

            String rawGenres = request.getParameter("genres");
            List<Genre> genreList = new ArrayList<>();
            if (rawGenres != null && !rawGenres.trim().isEmpty()) {
                String[] genreNames = InputSanitizer.sanitize(rawGenres).split(",");
                for (String genreName : genreNames) {
                    genreName = genreName.trim();
                    Genre genre = productDAO.findGenreByName(genreName);
                    System.out.println(genre);
                    if (genre != null) {
                        genreList.add(genre);
                    } else {
                        request.setAttribute("errorMessage", "Genere " + genreName + " non trovato.");
                        request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                        return;
                    }
                }
            }

            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setDescription(description);
            newProduct.setSalePrice(salePrice);
            newProduct.setOriginalPrice(originalPrice);
            newProduct.setAvailability(availability);
            newProduct.setReleaseDate(releaseDate);
            newProduct.setImage(image);
            newProduct.setSupportedDevice(device);
            newProduct.setDeleted(false);

            for (Artist artist : artistList) {
                newProduct.addArtist(artist);
            }
            for (Genre genre : genreList) {
                newProduct.addGenre(genre);
            }
            
            System.out.println(newProduct);
            productDAO.insertProduct(newProduct);

            response.sendRedirect("gestisciCatalogoProdottiControl");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Formato non valido per uno dei campi numerici.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore durante il salvataggio del prodotto nel database.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Si è verificato un errore inaspettato.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }
}
