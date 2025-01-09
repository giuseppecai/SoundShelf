package ordini;

import util.DataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import prodotti.Product;

public class OrderDAO {
    private DataSource dataSource;

    public OrderDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public int addOrder(Order order) throws SQLException {
        String sql = "INSERT INTO Ordine (emailCliente, prezzoTotale, dataAcquisto, dataConsegna, indirizzoSpedizione, stato) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, order.getEmailCliente());
            ps.setDouble(2, order.getPrezzoTotale());
            ps.setDate(3, order.getDataOrdine());
            ps.setDate(4, order.getDataConsegna());
            ps.setString(5, order.getIndirizzoSpedizione());
            ps.setString(6, order.getStato().getStato());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creazione ordine fallita.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creazione ordine fallita.");
                }
            }
        }
    }


    public synchronized Order getOrderById(int orderId) {
        String query = "SELECT * FROM Ordine WHERE numeroOrdine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Order(
                        resultSet.getInt("numeroOrdine"),
                        resultSet.getString("emailCliente"),
                        resultSet.getDouble("prezzoTotale"),
                        resultSet.getDate("dataAcquisto"),
                        resultSet.getDate("dataConsegna"),
                        resultSet.getString("indirizzoSpedizione"),
                        StatoOrdine.fromString(resultSet.getString("stato"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public synchronized List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Ordine";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order(
                    resultSet.getInt("numeroOrdine"),
                    resultSet.getString("emailCliente"),
                    resultSet.getDouble("prezzoTotale"),
                    resultSet.getDate("dataAcquisto"),
                    resultSet.getDate("dataConsegna"),
                    resultSet.getString("indirizzoSpedizione"),
                    StatoOrdine.fromString(resultSet.getString("stato"))
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void updateStatoOrder(Order order) {
        String sql = "UPDATE Ordine SET stato = ? WHERE numeroOrdine = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, order.getStato().name().replace("_", " "));
            ps.setInt(2, order.getNumeroOrdine());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void updateOrder(Order order) {
        String query = "UPDATE Ordine SET emailCliente = ?, prezzoTotale = ?, dataAcquisto = ?, dataConsegna = ?, indirizzoSpedizione = ?, stato = ? WHERE numeroOrdine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, order.getEmailCliente());
            statement.setDouble(2, order.getPrezzoTotale());
            statement.setDate(3, order.getDataOrdine());
            statement.setDate(4, order.getDataConsegna());
            statement.setString(5, order.getIndirizzoSpedizione());
            statement.setString(6, order.getStato().getStato());
            statement.setInt(7, order.getNumeroOrdine());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized void deleteOrder(int orderId) {
        String query = "DELETE FROM Ordine WHERE numeroOrdine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public synchronized List<Order> getOrdersByEmail(String emailCliente) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM Ordine WHERE emailCliente = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, emailCliente);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Order order = new Order();
                    order.setNumeroOrdine(resultSet.getInt("numeroOrdine"));
                    order.setEmailCliente(resultSet.getString("emailCliente"));
                    order.setPrezzoTotale(resultSet.getDouble("prezzoTotale"));
                    order.setDataOrdine(resultSet.getDate("dataAcquisto"));
                    order.setDataConsegna(resultSet.getDate("dataConsegna"));
                    order.setIndirizzoSpedizione(resultSet.getString("indirizzoSpedizione"));
                    order.setStato(StatoOrdine.fromString(resultSet.getString("stato")));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<Order> filterOrders(String emailCliente, Date dataInizio, Date dataFine) {
        List<Order> orders = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Ordine WHERE 1=1");

        if (emailCliente != null && !emailCliente.isEmpty()) {
            sql.append(" AND emailCliente = ?");
        }
        if (dataInizio != null) {
            sql.append(" AND dataAcquisto >= ?");
        }
        if (dataFine != null) {
            sql.append(" AND dataAcquisto <= ?");
        }

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;

            if (emailCliente != null && !emailCliente.isEmpty()) {
                ps.setString(paramIndex++, emailCliente);
            }
            if (dataInizio != null) {
                ps.setDate(paramIndex++, dataInizio);
            }
            if (dataFine != null) {
                ps.setDate(paramIndex++, dataFine);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setNumeroOrdine(rs.getInt("numeroOrdine"));
                    order.setEmailCliente(rs.getString("emailCliente"));
                    order.setPrezzoTotale(rs.getDouble("prezzoTotale"));
                    order.setDataOrdine(rs.getDate("dataAcquisto"));
                    order.setDataConsegna(rs.getDate("dataConsegna"));
                    order.setIndirizzoSpedizione(rs.getString("indirizzoSpedizione"));
                    order.setStato(StatoOrdine.fromString(rs.getString("stato")));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    
    public synchronized List<Product> getPurchasedProductsByEmail(String emailCliente) {
        List<Product> products = new ArrayList<>();
        String query = """
            SELECT p.id AS codiceProdotto, p.nome, p.descrizione, p.disponibilita, p.prezzoVendita, 
                   p.prezzoOriginale, p.formato, p.immagine
            FROM Prodotto p
            JOIN ElementoOrdine eo ON p.id = eo.idProdotto
            JOIN Ordine o ON eo.idOrdine = o.numeroOrdine
            JOIN UtenteRegistrato u ON o.emailCliente = u.email
            WHERE u.email = ?
        """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, emailCliente);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setProductCode(resultSet.getInt("codiceProdotto"));
                    product.setName(resultSet.getString("nome"));
                    product.setDescription(resultSet.getString("descrizione"));
                    product.setAvailability(resultSet.getInt("disponibilita"));
                    product.setSalePrice(resultSet.getDouble("prezzoVendita"));
                    product.setOriginalPrice(resultSet.getDouble("prezzoOriginale"));
                    product.setSupportedDevice(resultSet.getString("formato"));
                    product.setImage(resultSet.getString("immagine"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    
    public boolean updateOrderStatus(int numeroOrdine, String nuovoStato) throws SQLException {
        String query = "UPDATE Ordine SET stato = ? WHERE numeroOrdine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, nuovoStato);
            statement.setInt(2, numeroOrdine);
            return statement.executeUpdate() > 0;
        }
    }



}
