<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ricerca Prodotti</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
    <script src="${pageContext.request.contextPath}/scripts/searchProducts.js"></script>
</head>
<body>
    <jsp:include page="../pagePieces/header.jsp" />
    
    <div class="container">
        <h1>Ricerca Prodotti</h1>
        
        <form class="search-form" id="searchForm">
            <div class="form-group">
                <label for="productName">Nome prodotto:</label>
                <input type="text" id="productName" name="productName" placeholder="Inserisci il nome del prodotto" />
            </div>
            <div class="form-group">
                <label for="productArtist">Artista:</label>
                <input type="text" id="productArtist" name="productArtist" placeholder="Inserisci l'artista" />
            </div>
            <div class="form-group">
                <label for="productGenre">Genere:</label>
                <input type="text" id="productGenre" name="productGenre" placeholder="Inserisci il genere" />
            </div>
            <button type="submit" class="btn">Cerca</button>
        </form>
        
        <div id="errorMessage" style="color: red; display: none;"></div>
 
        
         <section id="prodotti" class="products-section">
            <div id="results" class="main-content grid"></div>
         </section>
    </div>
    
    <jsp:include page="../pagePieces/footer.jsp" />
</body>
</html>
