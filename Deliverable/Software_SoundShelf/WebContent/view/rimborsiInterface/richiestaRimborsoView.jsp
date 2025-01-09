<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, rimborsi.*" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <title>Le Tue Richieste di Rimborso</title>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2 class="page-title">Le Tue Richieste di Rimborso</h2>

        <%
            List<RefoundRequest> refoundRequests = (List<RefoundRequest>) request.getAttribute("refoundRequests");
            if (refoundRequests != null && !refoundRequests.isEmpty()) {
        %>
            <table class="requests-table">
                <thead>
                    <tr>
                        <th>Codice Ordine</th>
                        <th>Codice Prodotto</th>
                        <th>Motivo</th>
                        <th>IBAN</th>
                        <th>Stato</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (RefoundRequest refoundRequest : refoundRequests) {
                    %>
                    <tr>
                        <td><%= refoundRequest.getIdOrdine() %></td>
                        <td><%= refoundRequest.getIdProdotto() %></td>
                        <td><%= refoundRequest.getMotivo() %></td>
                        <td><%= refoundRequest.getIban() %></td>
                        <td><%= refoundRequest.getStato().getStato() %></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        <%
            } else {
        %>
            <div class="empty-request-card">
                <h3>Non hai ancora inviato richieste di rimborso.</h3>
                <p>Quando invierai la tua prima richiesta di rimborso, sarà visibile qui.</p>
            </div>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
