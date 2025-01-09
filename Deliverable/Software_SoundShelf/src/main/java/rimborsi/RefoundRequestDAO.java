package rimborsi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import util.DataSource;

public class RefoundRequestDAO {

    private DataSource dataSource;

    public RefoundRequestDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public void saveRichiestaRimborso(RefoundRequest richiestaRimborso) throws SQLException {
        String query = "INSERT INTO RichiestaRimborso (motivoRimborso, dataRichiesta, stato, ibanCliente, idOrdine, idProdotto, emailCliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, richiestaRimborso.getMotivo());
            statement.setDate(2, Date.valueOf(java.time.LocalDate.now()));
            statement.setString(3, richiestaRimborso.getStato().getStato());
            statement.setString(4, richiestaRimborso.getIban());
            statement.setInt(5, richiestaRimborso.getIdOrdine());
            statement.setInt(6, richiestaRimborso.getIdProdotto());
            statement.setString(7, richiestaRimborso.getEmailCliente());
            statement.executeUpdate();
        }
    }

    public void updateRichiestaRimborso(RefoundRequest richiestaRimborso) throws SQLException {
        String query = "UPDATE RichiestaRimborso SET motivoRimborso = ?, stato = ?, ibanCliente = ?, emailCliente = ? WHERE idOrdine = ? AND idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, richiestaRimborso.getMotivo());
            statement.setString(2, richiestaRimborso.getStato().getStato());
            statement.setString(3, richiestaRimborso.getIban());
            statement.setString(4, richiestaRimborso.getEmailCliente());
            statement.setInt(5, richiestaRimborso.getIdOrdine());
            statement.setInt(6, richiestaRimborso.getIdProdotto());
            statement.executeUpdate();
        }
    }

    public void deleteRichiestaRimborso(int idOrdine, int idProdotto) throws SQLException {
        String query = "DELETE FROM RichiestaRimborso WHERE idOrdine = ? AND idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idOrdine);
            statement.setInt(2, idProdotto);
            statement.executeUpdate();
        }
    }

    public RefoundRequest getRichiestaRimborso(int idOrdine, int idProdotto) throws SQLException {
        String query = "SELECT * FROM RichiestaRimborso WHERE idOrdine = ? AND idProdotto = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idOrdine);
            statement.setInt(2, idProdotto);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new RefoundRequest(
                            resultSet.getInt("id"),
                            resultSet.getString("motivoRimborso"),
                            resultSet.getString("ibanCliente"),
                            StatoRimborso.fromString(resultSet.getString("stato")),
                            resultSet.getInt("idOrdine"),
                            resultSet.getInt("idProdotto"),
                            resultSet.getString("emailCliente")
                    );
                }
            }
        }
        return null;
    }

    public List<RefoundRequest> getAllRichiesteRimborso() throws SQLException {
        List<RefoundRequest> richiesteRimborso = new ArrayList<>();
        String query = "SELECT * FROM RichiestaRimborso";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                richiesteRimborso.add(new RefoundRequest(
                        resultSet.getInt("id"),
                        resultSet.getString("motivoRimborso"),
                        resultSet.getString("ibanCliente"),
                        StatoRimborso.fromString(resultSet.getString("stato")),
                        resultSet.getInt("idOrdine"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getString("emailCliente")
                ));
            }
        }
        return richiesteRimborso;
    }

    public List<RefoundRequest> getRichiesteRimborsoPerUtente(String email) throws SQLException {
        List<RefoundRequest> richiesteRimborso = new ArrayList<>();
        String query = "SELECT * FROM RichiestaRimborso WHERE emailCliente = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                richiesteRimborso.add(new RefoundRequest(
                        resultSet.getInt("id"),
                        resultSet.getString("motivoRimborso"),
                        resultSet.getString("ibanCliente"),
                        StatoRimborso.fromString(resultSet.getString("stato")),
                        resultSet.getInt("idOrdine"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getString("emailCliente")
                ));
            }
        }
        return richiesteRimborso;
    }
}
