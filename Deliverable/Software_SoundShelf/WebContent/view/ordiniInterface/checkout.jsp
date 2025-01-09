<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="utente.UtenteRegistrato" %>
<%@ page import="ordini.Cart" %>
<%@ page import="ordini.CartItem" %>
<%@ page import="prodotti.Product" %>
<%
    Cart cart = (Cart) session.getAttribute("cart");
    UtenteRegistrato user = (UtenteRegistrato) session.getAttribute("user");
    String savedAddress = (user != null) ? user.getIndirizzo() : null;
    double totalPrice = (cart != null) ? cart.getTotalPrice() : 0;
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>SoundShelf - Checkout</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <script>
        function toggleNewAddressForm(checkbox) {
            const form = document.getElementById('newAddressForm');
            form.style.display = checkbox.checked ? 'block' : 'none';
        }
    </script>
</head>
<body>
    <div>
        <jsp:include page="../pagePieces/header.jsp" />
        <section class="checkout-container">
            <h2>Checkout</h2>

            <!-- Riepilogo Ordine -->
            <div class="order-summary">
                <h3>Riepilogo Ordine</h3>
                <table class="order-table">
                    <thead>
                        <tr>
                            <th>Prodotto</th>
                            <th>Quantit&agrave;</th>
                            <th>Prezzo Unitario</th>
                            <th>Totale</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% if (cart != null) {
                            for (CartItem item : cart.getItems()) {
                                Product product = item.getProduct();
                        %>
                        <tr>
                            <td><%= product.getName() %></td>
                            <td><%= item.getQuantity() %></td>
                            <td>&euro;<%= product.getSalePrice() %></td>
                            <td>&euro;<%= product.getSalePrice() * item.getQuantity() %></td>
                        </tr>
                        <%    }
                            } else { %>
                        <tr>
                            <td colspan="4">Il carrello è vuoto.</td>
                        </tr>
                        <% } %>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="3"><strong>Totale:</strong></td>
                            <td><strong>&euro;<%= totalPrice %></strong></td>
                        </tr>
                    </tfoot>
                </table>
            </div>

            <!-- Indirizzo di Spedizione -->
            <form action="${pageContext.request.contextPath}/acquistoControl" method="post" class="checkout-form">
                <div class="form-group">
                    <h3>Indirizzo di Spedizione</h3>
                    <% if (savedAddress != null) { %>
                        <p><strong>Indirizzo salvato:</strong> <%= savedAddress %></p>
                        <p>
                            <input type="checkbox" name="useNewAddress" id="useNewAddress" onchange="toggleNewAddressForm(this)">
                            <label for="useNewAddress">Usa un nuovo indirizzo</label>
                        </p>
                    <% } else { %>
                        <p>Inserisci il tuo indirizzo di spedizione.</p>
                    <% } %>
                </div>

                <div id="newAddressForm" style="<%= (savedAddress != null) ? "display:none;" : "display:block;" %>">
                    <h4>Nuovo Indirizzo</h4>
                    <label for="via">Via:</label>
                    <input type="text" name="via" id="via"><br>

                    <label for="numeroCivico">Numero Civico:</label>
                    <input type="text" name="numeroCivico" id="numeroCivico"><br>

                    <label for="cap">CAP:</label>
                    <input type="text" name="cap" id="cap"><br>

                    <label for="citta">Città:</label>
                    <input type="text" name="citta" id="citta"><br>

                    <label for="provincia">Provincia:</label>
                    <input type="text" name="provincia" id="provincia"><br>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-checkout">Procedi con l'Acquisto</button>
                </div>
            </form>

            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/carrelloControl">
                    <button type="button" class="btn-return">Annulla e Torna al Carrello</button>
                </a>
            </div>
        </section>
        <jsp:include page="../pagePieces/footer.jsp" />
    </div>
</body>
</html>
