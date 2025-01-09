<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*, recensione.*, prodotti.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Aggiungi Recensione</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <script>
        function confirmSubmission() {
            return confirm("Sei sicuro di voler inviare questa recensione?");
        }
    </script>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div class="review-page-container">
    <% 
        Product purchasedProduct = (Product) request.getAttribute("purchasedProduct");
        if (purchasedProduct != null) {
    %>
        <div class="review-form-container">
            <h2 class="review-form-title">Aggiungi una Recensione</h2>
            <form action="${pageContext.request.contextPath}/addReview" method="post" onsubmit="return confirmSubmission();">
                <fieldset class="review-fieldset">
                    <legend class="review-legend">Recensione Prodotto</legend>
                    <div class="review-form-group">
                        <label for="productName" class="review-label">Prodotto:</label>
                        <span id="productName" class="review-product-name"><%= purchasedProduct.getName() %></span>
                    </div>
                    <div class="review-form-group">
                        <label for="rating" class="review-label">Voto:</label>
                        <select name="rating" id="rating" class="review-input-select" required>
                            <option value="">-- Seleziona --</option>
                            <option value="1">0</option>
                            <option value="2">1</option>
                            <option value="3">2</option>
                            <option value="4">3</option>
                            <option value="5">4</option>
                            <option value="5">5</option>
                        </select>
                    </div>
                    <div class="review-form-group">
                        <label for="comment" class="review-label">Commento:</label>
                        <textarea name="comment" id="comment" class="review-input-textarea" rows="4" required></textarea>
                    </div>
                    <input type="hidden" name="productId" value="<%= purchasedProduct.getProductCode() %>" />
                    <div class="review-form-group">
                        <button type="submit" class="review-submit-btn">Invia Recensione</button>
                    </div>
                </fieldset>
            </form>
        </div>
    <% 
        } else {
    %>
        <h3 class="review-no-purchase-message">Non hai acquistato ancora nessun prodotto per lasciare una recensione.</h3>
    <% 
        } 
    %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
