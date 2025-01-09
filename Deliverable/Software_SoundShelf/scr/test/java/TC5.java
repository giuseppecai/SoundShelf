import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import prodotti.Artist;
import prodotti.CercaProdottiControl;
import prodotti.Genre;
import prodotti.Product;
import prodotti.ProductDAO;
import util.DataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TC5 {

    @InjectMocks
    private CercaProdottiControl cercaProdottiControl;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private PrintWriter writer;
    
    @Mock
    private DataSource dataSource;
    
    @Mock
    private Connection connection;

    @Mock
    private RequestDispatcher requestDispatcher;

    @BeforeEach
    void setUp() throws IOException, ServletException, SQLException {
        lenient().when(dataSource.getConnection()).thenReturn(connection);
        lenient().when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        DataSource.init("jdbc:mysql://localhost:3306/SoundShelf", "root", "W23e45f78.");
        when(response.getWriter()).thenReturn(writer);
        cercaProdottiControl.init();
        cercaProdottiControl.productDAO = productDAO;
    }

    @Test
    void testDoGet_withValidParameters() throws ServletException, IOException, SQLException {
        when(request.getParameter("name")).thenReturn("Lover");
        when(request.getParameter("genre")).thenReturn("Rock");
        when(request.getParameter("artist")).thenReturn("Drake");

        Product product = new Product(7, "Lover", List.of(new Artist("Drake", "Aubrey Graham", "Drake")), "2019-08-23", "Taylor Swift", 200, 21.99, 29.99, "CD", List.of(new Genre("Rock")), "image.jpg", false);
        List<Product> products = Arrays.asList(product);

        lenient().when(productDAO.searchProducts(anyString(), anyList(), anyList())).thenReturn(products);

        cercaProdottiControl.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("productCode", product.getProductCode());
        expectedJson.put("name", product.getName());
        expectedJson.put("description", product.getDescription());
        expectedJson.put("artists", product.getArtists());
        expectedJson.put("genres", product.getGenres());
        expectedJson.put("salePrice", product.getSalePrice());
        expectedJson.put("originalPrice", product.getOriginalPrice());
        expectedJson.put("image", product.getImage());
        expectedJson.put("availability", product.getAvailability());
        expectedJson.put("releaseDate", product.getReleaseDate());

        JSONArray expectedJsonArray = new JSONArray();
        expectedJsonArray.put(expectedJson);

        verify(writer).write(expectedJsonArray.toString());
    }

    @Test
    void testDoGet_withNoParameters() throws ServletException, IOException, SQLException {
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("genre")).thenReturn(null);
        when(request.getParameter("artist")).thenReturn(null);

        List<Product> products = Arrays.asList(
                new Product(1, "Greatest Hits", List.of(new Artist("Vasco", "Rossi", "Vasco Rossi")), "2022-01-01", "Le migliori canzoni di Vasco Rossi", 100, 19.99, 29.99, "Vinile", List.of(new Genre("Rock")), "image.jpg", false),
                new Product(2, "My Everything", List.of(new Artist("Ariana", "Grande", "Ariana Grande")), "2014-08-25", "Album di Ariana Grande", 200, 14.99, 24.99, "CD", List.of(new Genre("Pop")), "image.jpg", false),
                new Product(3, "Starboy", List.of(new Artist("The", "Weeknd", "The Weekend")), "2016-11-25", "Album di The Weeknd", 150, 17.99, 27.99, "Vinile", List.of(new Genre("Pop")), "image.jpg", false)
        );

        lenient().when(productDAO.searchProducts(null, null, null)).thenReturn(products);

        cercaProdottiControl.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        JSONArray expectedJsonArray = new JSONArray();
        for (Product p : products) {
            JSONObject expectedJson = new JSONObject();
            expectedJson.put("productCode", p.getProductCode());
            expectedJson.put("name", p.getName());
            expectedJson.put("description", p.getDescription());
            expectedJson.put("artists", p.getArtists());
            expectedJson.put("genres", p.getGenres());
            expectedJson.put("salePrice", p.getSalePrice());
            expectedJson.put("originalPrice", p.getOriginalPrice());
            expectedJson.put("image", p.getImage());
            expectedJson.put("availability", p.getAvailability());
            expectedJson.put("releaseDate", p.getReleaseDate());
            expectedJsonArray.put(expectedJson);
        }

        verify(writer).write(expectedJsonArray.toString());
    }

    @Test
    void testDoGet_withErrorInDAO() throws ServletException, IOException, SQLException {
        when(request.getParameter("name")).thenReturn("Lover");
        when(request.getParameter("genre")).thenReturn("Rock");
        when(request.getParameter("artist")).thenReturn("Drake");

        when(productDAO.searchProducts(anyString(), anyList(), anyList())).thenThrow(new SQLException("Errore nel database"));

        cercaProdottiControl.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(writer).write("{\"error\":\"Errore nella ricerca dei prodotti.\"}");
    }
}
