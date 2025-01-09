<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" import="java.util.*, prodotti.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Modifica Prodotto</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${pageContext.request.contextPath}/styles/style.css"
	rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/scripts/validation.js"></script>
<script>
	function confirmEdit() {
		return confirm("Sei sicuro di voler modificare questo prodotto?");
	}
</script>
</head>
<body>

	<jsp:include page="../pagePieces/header.jsp" />

	<div class="add-product-page-container">
		<h2 class="add-product-title">Modifica Prodotto</h2>

		<%
		Product product = (Product) request.getAttribute("product");
		if (product != null) {
		%>
		<form action="<%=request.getContextPath()%>/ModificaProdottoControl"
			method="post" onsubmit="return confirmEdit();">
			<fieldset class="add-product-fieldset">
				<legend class="add-product-legend">Modifica Dettagli del
					Prodotto</legend>

				<input type="hidden" name="productCode"
					value="<%=product.getProductCode()%>" />

				<div class="add-product-form-group">
					<label for="name" class="add-product-label">Nome Prodotto:</label>
					<input type="text" id="name" name="name" class="add-product-input"
						value="<%=product.getName()%>" required><br>
				</div>

				<div class="add-product-form-group">
					<label for="description" class="add-product-label">Descrizione:</label>
					<textarea id="description" name="description"
						class="add-product-textarea" required><%=product.getDescription()%></textarea>
					<br>
				</div>

				<div class="add-product-form-group">
					<label for="salePrice" class="add-product-label">Prezzo di
						Vendita (&euro;):</label> <input type="number" step="0.01" id="salePrice"
						name="salePrice" class="add-product-input"
						value="<%=product.getSalePrice()%>" required><br>
				</div>

				<div class="add-product-form-group">
					<label for="originalPrice" class="add-product-label">Prezzo
						Originale (&euro;):</label> <input type="number" step="0.01"
						id="originalPrice" name="originalPrice" class="add-product-input"
						value="<%=product.getOriginalPrice()%>" required><br>
				</div>

				<div class="add-product-form-group">
					<label for="availability" class="add-product-label">Disponibilit&aacute;:</label>
					<input type="number" id="availability" name="availability"
						class="add-product-input" value="<%=product.getAvailability()%>"
						required><br>
				</div>

				<div class="add-product-form-group">
					<label for="releaseDate" class="add-product-label">Data di
						Rilascio:</label> <input type="date" id="releaseDate" name="releaseDate"
						class="add-product-input" value="<%=product.getReleaseDate()%>"
						required><br>
				</div>

				<div class="add-product-form-group">
					<label for="image" class="add-product-label">Immagine URL:</label>
					<input type="text" id="image" name="image"
						class="add-product-input" value="<%=product.getImage()%>"
						required><br>
				</div>

				<div class="add-product-form-group">
					<label for="supportedDevice" class="add-product-label">Formato:</label>
					<select id="supportedDevice" name="supportedDevice"
						class="add-product-input" required>
						<option value="CD"
							<%="CD".equals(product.getSupportedDevice()) ? "selected" : ""%>>CD</option>
						<option value="Vinile"
							<%="Vinile".equals(product.getSupportedDevice()) ? "selected" : ""%>>Vinile</option>
					</select><br>
				</div>


				<div class="add-product-form-group">
					<button type="submit" class="add-product-submit-btn">Salva
						Modifiche</button>
				</div>
			</fieldset>
		</form>
		<%
		} else {
		%>
		<p>Errore: Prodotto non trovato.</p>
		<%
		}
		%>
	</div>

	<jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
