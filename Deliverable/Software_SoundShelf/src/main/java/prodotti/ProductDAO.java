package prodotti;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import util.DataSource;

public class ProductDAO {

    private DataSource dataSource;

    public ProductDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM Prodotto WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descrizione"),
                            rs.getInt("disponibilita"),
                            rs.getDouble("prezzoVendita"),
                            rs.getDouble("prezzoOriginale"),
                            rs.getString("formato"),
                            rs.getString("immagine"),
                            rs.getDate("dataPubblicazione").toString(), // Gestione della data
                            rs.getBoolean("isDeleted")
                    );
                    product.setArtists(getArtistsByProductId(productId));
                    product.setGenres(getGenresByProductId(productId));
                    return product;
                }
                return null;
            }
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        String query = "SELECT * FROM Prodotto WHERE isDeleted = 'true'";
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Product product = new Product(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("descrizione"),
                        rs.getInt("disponibilita"),
                        rs.getDouble("prezzoVendita"),
                        rs.getDouble("prezzoOriginale"),
                        rs.getString("formato"),
                        rs.getString("immagine"),
                        rs.getDate("dataPubblicazione").toString(),
                        rs.getBoolean("isDeleted")
                );
                product.setArtists(getArtistsByProductId(rs.getInt("id")));
                product.setGenres(getGenresByProductId(rs.getInt("id")));
                products.add(product);
            }
        }
        return products;
    }

    public void insertProduct(Product product) throws SQLException {
        String query = "INSERT INTO Prodotto (nome, descrizione, disponibilita, prezzoVendita, prezzoOriginale, formato, immagine, dataPubblicazione, isDeleted) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getAvailability());
            ps.setDouble(4, product.getSalePrice());
            ps.setDouble(5, product.getOriginalPrice());
            ps.setString(6, product.getSupportedDevice());
            ps.setString(7, product.getImage());
            ps.setDate(8, Date.valueOf(product.getReleaseDate()));
            ps.setBoolean(9, product.isDeleted());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setProductCode(generatedKeys.getInt(1));
                }
            }

            if (product.getArtists() != null) {
                for (Artist artist : product.getArtists()) {
                    insertProductArtist(product.getProductCode(), artist);
                }
            }

            if (product.getGenres() != null) {
                for (Genre genre : product.getGenres()) {
                    insertProductGenre(product.getProductCode(), genre);
                }
            }
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE Prodotto SET nome = ?, descrizione = ?, disponibilita = ?, prezzoVendita = ?, prezzoOriginale = ?, formato = ?, immagine = ?, dataPubblicazione = ?, isDeleted = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setInt(3, product.getAvailability());
            ps.setDouble(4, product.getSalePrice());
            ps.setDouble(5, product.getOriginalPrice());
            ps.setString(6, product.getSupportedDevice());
            ps.setString(7, product.getImage());
            ps.setDate(8, Date.valueOf(product.getReleaseDate()));
            ps.setBoolean(9, product.isDeleted());
            ps.setInt(10, product.getProductCode());
            ps.executeUpdate();

            updateProductArtists(product);
            updateProductGenres(product);
        }
    }

    public void deleteProduct(int productId) throws SQLException {
        String query = "UPDATE Prodotto SET isDeleted = TRUE WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    public List<Product> searchProducts(String name, List<String> genres, List<String> artists) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM Prodotto WHERE isDeleted = false");

        if (name != null && !name.isEmpty()) {
            queryBuilder.append(" AND nome LIKE ?");
        }
        if (genres != null && !genres.isEmpty()) {
            queryBuilder.append(" AND id IN (SELECT idProdotto FROM ProdottoGenere pg JOIN Genere g ON pg.idGenere = g.id WHERE g.nome IN (")
                    .append(String.join(",", Collections.nCopies(genres.size(), "?"))).append("))");
        }
        if (artists != null && !artists.isEmpty()) {
            queryBuilder.append(" AND id IN (SELECT idProdotto FROM ProdottoArtista pa JOIN Artista a ON pa.idArtista = a.id WHERE a.nomeArtistico IN (")
                    .append(String.join(",", Collections.nCopies(artists.size(), "?"))).append("))");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(queryBuilder.toString())) {

            int paramIndex = 1;

            if (name != null && !name.isEmpty()) {
                ps.setString(paramIndex++, "%" + name + "%");
            }
            if (genres != null && !genres.isEmpty()) {
                for (String genre : genres) {
                    ps.setString(paramIndex++, genre);
                }
            }
            if (artists != null && !artists.isEmpty()) {
                for (String artist : artists) {
                    ps.setString(paramIndex++, artist);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                List<Product> products = new ArrayList<>();
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("descrizione"),
                            rs.getInt("disponibilita"),
                            rs.getDouble("prezzoVendita"),
                            rs.getDouble("prezzoOriginale"),
                            rs.getString("formato"),
                            rs.getString("immagine"),
                            rs.getDate("dataPubblicazione").toString(),
                            rs.getBoolean("isDeleted")
                    );
                    product.setArtists(getArtistsByProductId(rs.getInt("id")));
                    product.setGenres(getGenresByProductId(rs.getInt("id")));
                    products.add(product);
                }
                return products;
            }
        }
    }

    private List<Artist> getArtistsByProductId(int productId) throws SQLException {
        String query = "SELECT a.nome, a.cognome, a.nomeArtistico FROM Artista a " +
                       "JOIN ProdottoArtista pa ON a.id = pa.idArtista " +
                       "WHERE pa.idProdotto = ?";
        List<Artist> artists = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    artists.add(new Artist(rs.getString("nome"), rs.getString("cognome"), rs.getString("nomeArtistico")));
                }
            }
        }
        return artists;
    }

    private List<Genre> getGenresByProductId(int productId) throws SQLException {
        String query = "SELECT g.nome FROM Genere g " +
                       "JOIN ProdottoGenere pg ON g.id = pg.idGenere " +
                       "WHERE pg.idProdotto = ?";
        List<Genre> genres = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    genres.add(new Genre(rs.getString("nome")));
                }
            }
        }
        return genres;
    }

    private void insertProductArtist(int productId, Artist artist) throws SQLException {
        String query = "INSERT INTO ProdottoArtista (idProdotto, idArtista) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, getArtistIdByName(artist.getFirstName(), artist.getLastName(), artist.getStageName()));
            ps.executeUpdate();
        }
    }

    private void insertProductGenre(int productId, Genre genre) throws SQLException {
        String query = "INSERT INTO ProdottoGenere (idProdotto, idGenere) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, getGenreIdByName(genre.getName()));
            ps.executeUpdate();
        }
    }

    private int getArtistIdByName(String firstName, String lastName, String stageName) throws SQLException {
        String query = "SELECT id FROM Artista WHERE nome = ? AND cognome = ? AND nomeArtistico = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, stageName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            }
        }
    }

    private int getGenreIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Genere WHERE nome = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
                return -1;
            }
        }
    }
    
    private void updateProductArtists(Product product) throws SQLException {
        String deleteQuery = "DELETE FROM ProdottoArtista WHERE idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setInt(1, product.getProductCode());
            ps.executeUpdate();
        }
        if (product.getArtists() != null) {
            for (Artist artist : product.getArtists()) {
                insertProductArtist(product.getProductCode(), artist);
            }
        }
    }

    private void updateProductGenres(Product product) throws SQLException {
        String deleteQuery = "DELETE FROM ProdottoGenere WHERE idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setInt(1, product.getProductCode());
            ps.executeUpdate();
        }
        if (product.getGenres() != null) {
            for (Genre genre : product.getGenres()) {
                insertProductGenre(product.getProductCode(), genre);
            }
        }
    }
    
    public Genre findGenreByName(String name) throws SQLException {
        String query = "SELECT * FROM Genere WHERE nome = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Genre(rs.getString("nome"));
                }
            }
        }
        return null;
    }

    public Artist findArtistByName(String stageName) throws SQLException {
        String query = "SELECT * FROM Artista WHERE nomeArtistico = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, stageName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Artist(rs.getString("nome"), rs.getString("cognome"), rs.getString("nomeArtistico"));
                }
            }
        }
        return null;
    }

    public String getImageByProductId(int productCode) throws SQLException {
        String query = "SELECT immagine FROM Prodotto WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, productCode);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("immagine");
                }
            }
        }
        return null;
    }

	public List<Product> getAllProductsHome() throws SQLException {
		 String query = "SELECT * FROM Prodotto WHERE isDeleted = 'false' AND disponibilita > 0";
	        List<Product> products = new ArrayList<>();
	        try (Connection connection = dataSource.getConnection();
	             Statement stmt = connection.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            while (rs.next()) {
	                Product product = new Product(
	                        rs.getInt("id"),
	                        rs.getString("nome"),
	                        rs.getString("descrizione"),
	                        rs.getInt("disponibilita"),
	                        rs.getDouble("prezzoVendita"),
	                        rs.getDouble("prezzoOriginale"),
	                        rs.getString("formato"),
	                        rs.getString("immagine"),
	                        rs.getDate("dataPubblicazione").toString(),
	                        rs.getBoolean("isDeleted")
	                );
	                product.setArtists(getArtistsByProductId(rs.getInt("id")));
	                product.setGenres(getGenresByProductId(rs.getInt("id")));
	                products.add(product);
	            }
	        }
	        return products;
	}


}
