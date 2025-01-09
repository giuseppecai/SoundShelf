package recensione;

import util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private DataSource dataSource;

    public ReviewDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public void saveReview(Review review) throws SQLException {
        String query = "INSERT INTO Recensioni (voto, descrizione, emailCliente, idProdotto, dataRecensione) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, review.getVoto());
            statement.setString(2, review.getDescrizione());
            statement.setString(3, review.getEmailCliente());
            statement.setInt(4, review.getIdProdotto());
            statement.setDate(5, review.getDataRecensione());
            statement.executeUpdate();
        }
    }

    public void updateReview(Review review) throws SQLException {
        String query = "UPDATE Recensioni SET voto = ?, descrizione = ?, emailCliente = ?, idProdotto = ?, dataRecensione = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, review.getVoto());
            statement.setString(2, review.getDescrizione());
            statement.setString(3, review.getEmailCliente());
            statement.setInt(4, review.getIdProdotto());
            statement.setDate(5, review.getDataRecensione());
            statement.setInt(6, review.getId());
            statement.executeUpdate();
        }
    }

    public void deleteReview(int reviewId) throws SQLException {
        String query = "DELETE FROM Recensioni WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reviewId);
            statement.executeUpdate();
        }
    }

    public Review getReviewById(int reviewId) throws SQLException {
        String query = "SELECT * FROM Recensioni WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, reviewId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Review(
                            resultSet.getInt("id"),
                            resultSet.getInt("voto"),
                            resultSet.getString("descrizione"),
                            resultSet.getString("emailCliente"),
                            resultSet.getInt("idProdotto"),
                            resultSet.getDate("dataRecensione")
                    );
                }
            }
        }
        return null;
    }

    public List<Review> getAllReviews() throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Recensioni";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                reviews.add(new Review(
                        resultSet.getInt("id"),
                        resultSet.getInt("voto"),
                        resultSet.getString("descrizione"),
                        resultSet.getString("emailCliente"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getDate("dataRecensione")
                ));
            }
        }
        return reviews;
    }

    public List<Review> getReviewsByProductId(int productId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT * FROM Recensioni WHERE idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, productId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    reviews.add(new Review(
                            resultSet.getInt("id"),
                            resultSet.getInt("voto"),
                            resultSet.getString("descrizione"),
                            resultSet.getString("emailCliente"),
                            resultSet.getInt("idProdotto"),
                            resultSet.getDate("dataRecensione")
                    ));
                }
            }
        }
        return reviews;
    }
}
