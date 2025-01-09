package ordini;

import java.util.ArrayList;
import java.util.List;

import prodotti.Product;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getProductCode() == product.getProductCode()) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product, 1));
    }

    public void removeProduct(Product product) {
        items.removeIf(item -> item.getProduct().getProductCode() == product.getProductCode());
    }

    public void updateQuantity(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getProductCode() == product.getProductCode()) {
                if (quantity > 0) {
                    item.setQuantity(quantity);
                } else {
                    items.remove(item);
                }
                return;
            }
        }
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotalPrice();
        }
        return total;
    }
}
