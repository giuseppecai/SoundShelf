<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*, prodotti.*, ordini.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Carrello</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        function confirmRemove() {
            return confirm("Sei sicuro di voler rimuovere questo prodotto dal carrello?");
        }

        function confirmClear() {
            return confirm("Sei sicuro di voler svuotare l'intero carrello?");
        }

        function validateQuantity(form) {
            var quantity = form.quantity.value;
            if (quantity < 1) {
                alert("La quantità non può essere inferiore a 1.");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <div class="cart-container">
        <% 
            Cart cart = (Cart) request.getSession().getAttribute("cart");
            if(cart != null && !cart.getItems().isEmpty()) { 
        %>

        <section class="cart-section">
            <h2>Il tuo Carrello</h2>
            <table class="cart-table">
                <tr>
                    <th>Descrizione</th>
                    <th>Quantit&aacute;</th>
                    <th>Prezzo totale</th>
                    <th>Rimuovi</th>
                </tr>
                <% 
                    for(CartItem item : cart.getItems()) {
                        Product product = item.getProduct();
                %>
                <tr>
                    <td><%= product.getName() %></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/carrelloControl" method="get" onsubmit="return validateQuantity(this)">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="productId" value="<%= product.getProductCode() %>">
                            <input type="number" name="quantity" value="<%= item.getQuantity() %>" min="1" class="quantity-input">
                            <button type="submit" class="button">Aggiorna</button>
                        </form>
                    </td>
                    <td>&euro;<%= String.format("%.2f", item.getTotalPrice()) %></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/carrelloControl" method="get" onsubmit="return confirmRemove()">
                            <input type="hidden" name="action" value="remove">
                            <input type="hidden" name="productId" value="<%= product.getProductCode() %>">
                            <button type="submit" class="remove-button button">Rimuovi</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </table>

            <div class="cart-total">
                Totale provvisorio: &euro;<%= String.format("%.2f", cart.getTotalPrice()) %>
            </div>

            <div class="cart-checkout">
                <a href="${pageContext.request.contextPath}/acquistoControl"> 
                    <button class="button">Acquista</button>
                </a>
            </div>

            <div class="cart-clear">
                <form action="${pageContext.request.contextPath}/carrelloControl" method="get" onsubmit="return confirmClear()">
                    <input type="hidden" name="action" value="clear">
                    <button type="submit" class="clear-button button">Svuota Carrello</button>
                </form>
            </div>

        </section>

        <% } else { %>
        <section class="cart-section">
            <h2>Il tuo carrello è vuoto</h2>
        </section>
        <% } %>
        </div> <!-- Fine .cart-container -->
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
