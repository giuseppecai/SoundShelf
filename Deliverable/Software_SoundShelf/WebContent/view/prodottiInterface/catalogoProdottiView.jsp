<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*, prodotti.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Catalogo Prodotti</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2>Catalogo Prodotti</h2>

        <div class="product-actions">
            <form action="view/prodottiInterface/inserisciProdottoForm.jsp" method="get">
                <button type="submit" class="btn-add">Aggiungi Nuovo Prodotto</button>
            </form>
        </div>

        <%
            List<Product> products = (List<Product>) request.getAttribute("products");
            if (products != null && !products.isEmpty()) {
        %>

        <table class="product-table">
            <thead>
                <tr>
                    <th>Immagine</th>
                    <th>Nome</th>
                    <th>Prezzo</th>
                    <th>Disponibilit&aacute;</th>
                    <th>Data di Rilascio</th>
                    <th>Descrizione</th>
                    <th>Artisti</th>
                    <th>Generi</th>
                    <th>Azioni</th>
                </tr>
            </thead>
            <tbody>
                <% for (Product product : products) { %>
                    <tr>
                        <td>
                            <img src="${pageContext.request.contextPath}/img/<%= product.getImage() != null ? product.getImage() : "default.jpg" %>" alt="<%= product.getName() %>" width="100">
                        </td>
                        <td><%= product.getName() %></td>
                        <td>&euro;<%= product.getSalePrice() %></td>
                        <td><%= product.getAvailability() %></td>
                        <td><%= product.getReleaseDate() %></td>
                        <td><%= product.getDescription() %></td>

                        <td class="artists">
                            <% 
                                        List<prodotti.Artist> artists = product.getArtists();
                                        if (artists != null && !artists.isEmpty()) {
                                            for (int i = 0; i < artists.size(); i++) {
                                                out.print(artists.get(i).getStageName());
                                                if (i < artists.size() - 1) {
                                                    out.print(", ");
                                                }
                                            }
                                        } else {
                                            out.print("Nessun artista disponibile");
                                        }
                             %>
                        </td>

                        <td class="genres">
                            <% 
                                        List<prodotti.Genre> genres = product.getGenres();
                                        if (genres != null && !genres.isEmpty()) {
                                            for (int i = 0; i < genres.size(); i++) {
                                                out.print(genres.get(i).getName());
                                                if (i < genres.size() - 1) {
                                                    out.print(", ");
                                                }
                                            }
                                        } else {
                                            out.print("Nessun genere disponibile");
                                        }
                             %>
                        </td>

                        <td>
                            <form action="${pageContext.request.contextPath}/prodottoControl" method="get" style="display:inline;">
                                <input type="hidden" name="productId" value="<%= product.getProductCode() %>" />
                                <button type="submit" class="btn-details">Dettagli</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/ModificaProdottoControl" method="get" style="display:inline;">
                                <input type="hidden" name="productId" value="<%= product.getProductCode() %>" />
                                <button type="submit" class="btn-edit">Modifica</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/rimuoviProdottoControl" method="post" style="display:inline;">
                                <input type="hidden" name="productId" value="<%= product.getProductCode() %>" />
                                <button type="submit" class="btn-delete" onclick="return confirm('Sei sicuro di voler rimuovere questo prodotto?')">Rimuovi</button>
                            </form>
                        </td>
                    </tr>
                <% } %>
            </tbody>
        </table>

        <% 
            } else {
        %>
        <p>Nessun prodotto disponibile.</p>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
