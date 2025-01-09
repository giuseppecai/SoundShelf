<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*, recensione.*, control.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Catalogo Recensioni</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2>Catalogo Recensioni</h2>

        <%
            List<Review> recensioni = (List<Review>) request.getAttribute("recensioni");
            if (recensioni != null && !recensioni.isEmpty()) {
        %>

        <div class="review-table">
            <table class="review-table">
                <thead>
                    <tr>
                        <th>Prodotto</th>
                        <th>Votazione</th>
                        <th>Email Cliente</th>
                        <th>Data Recensione</th>
                        <th>Testo</th>
                        <th>Azioni</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Review recensione : recensioni) { %>
                        <tr>
                            <td><%= recensione.getIdProdotto() %></td>
                            <td><%= recensione.getVoto() %>/5</td>
                            <td><%= recensione.getEmailCliente() %></td>
                            <td><%= recensione.getDataRecensione() %></td>
                            <td><%= recensione.getDescrizione() %></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/rimuoviRecensioneControl" method="post" style="display:inline;">
                                    <input type="hidden" name="reviewId" value="<%= recensione.getId() %>" />
                                    <button type="submit" class="btn-delete" onclick="return confirm('Sei sicuro di voler eliminare questa recensione?')">Elimina</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

        <% 
            } else {
        %>
        <p>Nessuna recensione disponibile.</p>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
