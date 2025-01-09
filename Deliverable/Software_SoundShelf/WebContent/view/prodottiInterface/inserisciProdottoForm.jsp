<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*, ordini.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Aggiungi Nuovo Prodotto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <script src="${pageContext.request.contextPath}/scripts/validation.js"></script>
    <script type="text/javascript">
        function confirmAddProduct() {
            return confirm('Sei sicuro di voler aggiungere questo nuovo prodotto?');
        }
    </script>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div class="add-product-page-container">
        <h2 class="add-product-title">Aggiungi Nuovo Prodotto</h2>
        <form action="${pageContext.request.contextPath}/InserisciNuovoProdottoControl" method="post" onsubmit="return confirmAddProduct();">
            <fieldset class="add-product-fieldset">
                <legend class="add-product-legend">Inserisci Dettagli del Prodotto</legend>

                <div class="add-product-form-group">
                    <label for="name" class="add-product-label">Nome Prodotto:</label>
                    <input type="text" id="name" name="name" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="description" class="add-product-label">Descrizione:</label>
                    <textarea id="description" name="description" class="add-product-textarea" required></textarea><br>
                </div>

                <div class="add-product-form-group">
                    <label for="salePrice" class="add-product-label">Prezzo di Vendita (&euro;):</label>
                    <input type="number" step="0.01" id="salePrice" name="salePrice" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="originalPrice" class="add-product-label">Prezzo Originale (&euro;):</label>
                    <input type="number" step="0.01" id="originalPrice" name="originalPrice" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="availability" class="add-product-label">Disponibilit&aacute;:</label>
                    <input type="number" id="availability" name="availability" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="releaseDate" class="add-product-label">Data di Rilascio:</label>
                    <input type="date" id="releaseDate" name="releaseDate" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="image" class="add-product-label">Immagine URL:</label>
                    <input type="text" id="image" name="image" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
    				<label for="supportedDevice" class="add-product-label">Formato:</label>
    				<select id="supportedDevice" name="supportedDevice" class="add-product-input" required>
        				<option value="CD">CD</option>
        				<option value="Vinile">Vinile</option>
    				</select><br>
				</div>


                <div class="add-product-form-group">
                    <label for="artists" class="add-product-label">Artisti (separati da virgola):</label>
                    <input type="text" id="artists" name="artists" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <label for="genres" class="add-product-label">Generi (separati da virgola):</label>
                    <input type="text" id="genres" name="genres" class="add-product-input" required><br>
                </div>

                <div class="add-product-form-group">
                    <button type="submit" class="add-product-submit-btn">Aggiungi Prodotto</button>
                </div>
            </fieldset>
        </form>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
