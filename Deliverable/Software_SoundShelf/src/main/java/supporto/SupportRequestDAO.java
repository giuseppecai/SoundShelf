package supporto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import util.DataSource;

public class SupportRequestDAO {

    private DataSource dataSource;

    public SupportRequestDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public void saveSupportRequest(SupportRequest supportRequest) throws SQLException {
        String query = "INSERT INTO RichiestaSupporto (descrizioneRichiesta, dataInvio, orarioInvio, stato, informazioniAggiuntive, rispostaUtente, idCliente) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supportRequest.getDescription());
            statement.setDate(2, supportRequest.getDataInvio());
            statement.setString(3, supportRequest.getOrarioInvio());
            statement.setString(4, supportRequest.getStato().getStato());
            statement.setString(5, supportRequest.getInformazioniAggiuntive());
            statement.setString(6, supportRequest.getRispostaUtente());
            statement.setInt(7, supportRequest.getIdCliente());
            statement.executeUpdate();
        }
    }


    public void updateSupportRequest(SupportRequest supportRequest) throws SQLException {
        String query = "UPDATE RichiestaSupporto SET descrizioneRichiesta = ?, dataInvio = ?, orarioInvio = ?, stato = ?, informazioniAggiuntive = ?, rispostaUtente = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, supportRequest.getDescription());
            statement.setDate(2, supportRequest.getDataInvio());
            statement.setString(3, supportRequest.getOrarioInvio());
            statement.setString(4, supportRequest.getStato().getStato());
            statement.setString(5, supportRequest.getInformazioniAggiuntive());
            statement.setString(6, supportRequest.getRispostaUtente());
            statement.setInt(7, supportRequest.getId());
            statement.executeUpdate();
        }
    }


    public SupportRequest getSupportRequestById(int id) throws SQLException {
        String query = "SELECT * FROM RichiestaSupporto WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new SupportRequest(
                            resultSet.getInt("id"),
                            resultSet.getString("descrizioneRichiesta"),
                            resultSet.getDate("dataInvio"),
                            resultSet.getString("orarioInvio"),
                            StatoSupporto.fromString(resultSet.getString("stato")),
                            resultSet.getString("informazioniAggiuntive"),
                            resultSet.getString("rispostaUtente"),
                            resultSet.getInt("idCliente")
                    );
                }
            }
        }
        return null;
    }


    public List<SupportRequest> getAllSupportRequests() throws SQLException {
        List<SupportRequest> supportRequests = new ArrayList<>();
        String query = "SELECT * FROM RichiestaSupporto";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supportRequests.add(new SupportRequest(
                        resultSet.getInt("id"),
                        resultSet.getString("descrizioneRichiesta"),
                        resultSet.getDate("dataInvio"),
                        resultSet.getString("orarioInvio"),
                        StatoSupporto.fromString(resultSet.getString("stato")),
                        resultSet.getString("informazioniAggiuntive"),
                        resultSet.getString("rispostaUtente"),
                        resultSet.getInt("idCliente")
                ));
            }
        }
        return supportRequests;
    }
    
    public List<SupportRequest> getSupportRequestsByEmail(String email) throws SQLException {
        List<SupportRequest> supportRequests = new ArrayList<>();
        String query = "SELECT r.* FROM RichiestaSupporto r " +
                       "JOIN UtenteRegistrato u ON r.idCliente = u.id " +
                       "WHERE u.email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                supportRequests.add(new SupportRequest(
                        resultSet.getInt("id"),
                        resultSet.getString("descrizioneRichiesta"),
                        resultSet.getDate("dataInvio"),
                        resultSet.getString("orarioInvio"),
                        StatoSupporto.fromString(resultSet.getString("stato")),
                        resultSet.getString("informazioniAggiuntive"),
                        resultSet.getString("rispostaUtente"),
                        resultSet.getInt("idCliente")
                ));
            }
        }
        return supportRequests;
    }

}
