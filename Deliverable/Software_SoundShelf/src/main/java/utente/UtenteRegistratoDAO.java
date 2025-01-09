package utente;

import util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UtenteRegistratoDAO {
    private DataSource dataSource;

    public UtenteRegistratoDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public synchronized UtenteRegistrato getUserByEmail(String email) {
        String query = "SELECT * FROM UtenteRegistrato WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new UtenteRegistrato(
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("nome"),
                        resultSet.getString("cognome"),
                        resultSet.getString("indirizzo"),
                        resultSet.getString("telefono"),
                        Ruolo.fromString(resultSet.getString("ruolo"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void addUser(UtenteRegistrato utente) {
        String query = "INSERT INTO UtenteRegistrato (email, password, nome, cognome, indirizzo, telefono, ruolo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, utente.getEmail());
            statement.setString(2, utente.getPasswordUser());
            statement.setString(3, utente.getNome());
            statement.setString(4, utente.getCognome());
            statement.setString(5, utente.getIndirizzo());
            statement.setString(6, utente.getTelefono());
            statement.setString(7, "Cliente");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateUser(UtenteRegistrato utente) {
        String query = "UPDATE UtenteRegistrato SET password = ?, nome = ?, cognome = ?, indirizzo = ?, telefono = ?, ruolo = ? WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, utente.getPasswordUser());
            statement.setString(2, utente.getNome());
            statement.setString(3, utente.getCognome());
            statement.setString(4, utente.getIndirizzo());
            statement.setString(5, utente.getTelefono());
            statement.setString(6, utente.getRuolo().getRuolo());
            statement.setString(7, utente.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deleteUser(String email) {
        String query = "DELETE FROM UtenteRegistrato WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<UtenteRegistrato> getAllUsers() {
        List<UtenteRegistrato> users = new ArrayList<>();
        String query = "SELECT * FROM UtenteRegistrato";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                UtenteRegistrato utente = new UtenteRegistrato(
                    resultSet.getString("email"),
                    resultSet.getString("password"),
                    resultSet.getString("nome"),
                    resultSet.getString("cognome"),
                    resultSet.getString("indirizzo"),
                    resultSet.getString("telefono"),
                    Ruolo.fromString(resultSet.getString("ruolo"))
                );
                users.add(utente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public synchronized UtenteRegistrato authenticate(String email, String password) {
        String query = "SELECT * FROM UtenteRegistrato WHERE email = ? AND password = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UtenteRegistrato user = new UtenteRegistrato();
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordUser(resultSet.getString("password"));
                    user.setNome(resultSet.getString("nome"));
                    user.setCognome(resultSet.getString("cognome"));
                    user.setIndirizzo(resultSet.getString("indirizzo"));
                    user.setTelefono(resultSet.getString("telefono"));
                    user.setRuolo(Ruolo.fromString(resultSet.getString("ruolo")));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void promoteToAdmin(String email) {
        String sql = "UPDATE UtenteRegistrato SET ruolo = 'GestoreSito' WHERE email = ?";
        try (Connection conn = DataSource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized int getUserIdByEmail(String email) {
        String query = "SELECT id FROM UtenteRegistrato WHERE email = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
