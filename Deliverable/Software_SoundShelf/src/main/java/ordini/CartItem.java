package ordini;

import java.sql.SQLException;

import prodotti.Product;
import prodotti.ProductDAO;

public class CartItem {
    private Product product;
    private int quantity;
    private ProductDAO productDAO = new ProductDAO();

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getSalePrice() * quantity;
    }
    
    public int getCodiceProdotto() {
        return product.getProductCode();
    }
    
    public String getImageItem() throws SQLException {
		return productDAO.getImageByProductId(product.getProductCode());
    }
}

