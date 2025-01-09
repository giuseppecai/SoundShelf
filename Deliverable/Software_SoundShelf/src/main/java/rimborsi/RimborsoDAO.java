package rimborsi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DataSource;

public class RimborsoDAO {

    private DataSource dataSource;

    public RimborsoDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public void creaRimborso(Rimborso rimborso) throws SQLException {
        String query = "INSERT INTO Rimborso (importoRimborso, dataEmissione, idRichiesta) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setDouble(1, rimborso.getImportoRimborso());
            statement.setDate(2, rimborso.getDataEmissione());
            statement.setInt(3, rimborso.getIdRichiesta());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        rimborso.setId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public Rimborso getRimborsoById(int id) throws SQLException {
        String query = "SELECT * FROM Rimborso WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Rimborso(
                            resultSet.getInt("id"),
                            resultSet.getDouble("importoRimborso"),
                            resultSet.getDate("dataEmissione"),
                            resultSet.getInt("idRichiesta")
                    );
                }
            }
        }
        return null;
    }

    public List<Rimborso> getAllRimborsi() throws SQLException {
        List<Rimborso> rimborsi = new ArrayList<>();
        String query = "SELECT * FROM Rimborso";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                rimborsi.add(new Rimborso(
                        resultSet.getInt("id"),
                        resultSet.getDouble("importoRimborso"),
                        resultSet.getDate("dataEmissione"),
                        resultSet.getInt("idRichiesta")
                ));
            }
        }
        return rimborsi;
    }

    public void aggiornaRimborso(Rimborso rimborso) throws SQLException {
        String query = "UPDATE Rimborso SET importoRimborso = ?, dataEmissione = ?, idRichiesta = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, rimborso.getImportoRimborso());
            statement.setDate(2, rimborso.getDataEmissione());
            statement.setInt(3, rimborso.getIdRichiesta());
            statement.setInt(4, rimborso.getId());
            statement.executeUpdate();
        }
    }

    public void eliminaRimborso(int id) throws SQLException {
        String query = "DELETE FROM Rimborso WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
