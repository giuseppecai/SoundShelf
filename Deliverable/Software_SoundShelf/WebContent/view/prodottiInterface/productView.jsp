<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="prodotti.Product, recensione.Review"%>
<%@ page import="java.util.List"%>
<%
Product product = (Product) request.getAttribute("product");
List<Review> reviews = (List<Review>) request.getAttribute("reviews");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SoundShelf - <%=product.getName()%></title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>
    <jsp:include page="../pagePieces/header.jsp" />

    <div class="container">
        <section class="product-view">
            <div class="product-image">
                <img src="${pageContext.request.contextPath}/img/<%= product.getImage() != null ? product.getImage() : "default.jpg" %>" alt="<%= product.getName() %>">
            </div>
            <div class="product-details">
                <h1><%=product.getName()%></h1>
                <p><strong>Artisti:</strong> <%=product.getArtists().stream().map(artist -> artist.getStageName()).collect(java.util.stream.Collectors.joining(", "))%></p>
                <p><strong>Data di Rilascio:</strong> <%=product.getReleaseDate()%></p>
                <p><strong>Generi:</strong> <%=product.getGenres().stream().map(genre -> genre.getName()).collect(java.util.stream.Collectors.joining(", "))%></p>
                <p><strong>Prezzo Scontato:</strong> &euro;<%=product.getSalePrice()%></p>
                <p><strong>Prezzo Originale:</strong> &euro;<%=product.getOriginalPrice()%></p>
                <p><strong>Disponibilit&aacute;:</strong> <%=product.getAvailability()%></p>
                <p><strong>Formato:</strong> <%= product.getSupportedDevice() %></p>

                <form action="${pageContext.request.contextPath}/carrelloControl" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="productId" value="<%=product.getProductCode()%>">
                    <button type="submit" class="button">Aggiungi al Carrello</button>
                </form>
            </div>
        </section>

        <section class="reviews-section">
            <h2>Recensioni</h2>
            <div class="reviews">
                <% if (reviews != null && !reviews.isEmpty()) { %>
                    <% for (Review review : reviews) { %>
                        <div class="review">
                            <p><strong>Email utente:</strong> <%=review.getEmailCliente()%></p>
                            <p><strong>Voto:</strong> <%=review.getVoto()%>/5</p>
                            <p><%=review.getDescrizione()%></p>
                            <p><small><%=review.getDataRecensione()%></small></p>
                        </div>
                    <% } %>
                <% } else { %>
                    <p>Non ci sono recensioni per questo prodotto.</p>
                <% } %>
            </div>
        </section>
    </div>

   <jsp:include page="../pagePieces/footer.jsp" />
</body>
</html>
