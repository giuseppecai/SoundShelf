package ordini;

import util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ElementoOrdineDAO {
    private DataSource dataSource;

    public ElementoOrdineDAO() {
        this.dataSource = DataSource.getInstance();
    }

    public void addOrderDetail(ElementoOrdine elementoOrdine) throws SQLException {
        String sql = "INSERT INTO ElementoOrdine (idOrdine, idProdotto, quantità, prezzoUnitario) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, elementoOrdine.getIdOrdine());
            ps.setInt(2, elementoOrdine.getIdProdotto());
            ps.setInt(3, elementoOrdine.getQuantita());
            ps.setDouble(4, elementoOrdine.getPrezzoUnitario());
            ps.executeUpdate();
        }
    }

    public List<ElementoOrdine> getOrderDetailsByOrderId(int ordineId) {
        List<ElementoOrdine> orderDetails = new ArrayList<>();
        String query = "SELECT * FROM ElementoOrdine WHERE idOrdine = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, ordineId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ElementoOrdine orderDetail = new ElementoOrdine(
                        resultSet.getInt("id"),
                        resultSet.getInt("idOrdine"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getInt("quantità"),
                        resultSet.getDouble("prezzoUnitario")
                    );
                    orderDetails.add(orderDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderDetails;
    }

    public void updateOrderDetail(ElementoOrdine orderDetail) {
        String query = "UPDATE ElementoOrdine SET quantità = ?, prezzoUnitario = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, orderDetail.getQuantita());
            statement.setDouble(2, orderDetail.getPrezzoUnitario());
            statement.setInt(3, orderDetail.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrderDetail(int id) {
        String query = "DELETE FROM ElementoOrdine WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ElementoOrdine> getAllOrderDetails() {
        List<ElementoOrdine> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM ElementoOrdine";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ElementoOrdine orderDetail = new ElementoOrdine(
                    rs.getInt("id"),
                    rs.getInt("idOrdine"),
                    rs.getInt("idProdotto"),
                    rs.getInt("quantità"),
                    rs.getDouble("prezzoUnitario")
                );
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetails;
    }
    
    public ElementoOrdine getOrderDetailsById(int detailId) {
        ElementoOrdine orderDetail = null;
        String query = "SELECT * FROM ElementoOrdine WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, detailId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    orderDetail = new ElementoOrdine(
                        resultSet.getInt("id"),
                        resultSet.getInt("idOrdine"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getInt("quantità"),
                        resultSet.getDouble("prezzoUnitario")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetail;
    }
    
    public ElementoOrdine getOrderDetailsByProductId(int detailId) {
        ElementoOrdine orderDetail = null;
        String query = "SELECT * FROM ElementoOrdine WHERE idProdotto = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, detailId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    orderDetail = new ElementoOrdine(
                        resultSet.getInt("id"),
                        resultSet.getInt("idOrdine"),
                        resultSet.getInt("idProdotto"),
                        resultSet.getInt("quantità"),
                        resultSet.getDouble("prezzoUnitario")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderDetail;
    }

}