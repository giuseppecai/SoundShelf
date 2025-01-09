<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, rimborsi.*" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <title>Gestione Richieste di Rimborso</title>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2 class="page-title">Gestione Richieste di Rimborso</h2>

        <%
            List<RefoundRequest> richieste = (List<RefoundRequest>) request.getAttribute("richieste");
            if (richieste != null && !richieste.isEmpty()) {
        %>
            <table class="requests-table">
                <thead>
                    <tr>
                        <th>Codice Prodotto</th>
                        <th>Codice Ordine</th>
                        <th>Motivo</th>
                        <th>IBAN</th>
                        <th>Stato Attuale</th>
                        <th>Nuovo Stato</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (RefoundRequest richiesta : richieste) {
                    %>
                    <tr>
                        <td><%= richiesta.getIdProdotto() %></td>
                        <td><%= richiesta.getIdOrdine() %></td>
                        <td><%= richiesta.getMotivo() %></td>
                        <td><%= richiesta.getIban() %></td>
                        <td><%= richiesta.getStato().getStato() %></td>
                        <td>
                        	<form action="${pageContext.request.contextPath}/gestisciRichiesteRimborsoControl" method="post">
                        	<input type="hidden" name="productId" value="<%= richiesta.getIdProdotto() %>" />
                        	<input type="hidden" name="orderId" value="<%= richiesta.getIdOrdine() %>" />
                				<button type="submit" class="refund-button">Salva Modifiche</button>
                            	<select name="newState">
                                	<option value="In revisione" <%= richiesta.getStato().equals(StatoRimborso.IN_REVISIONE) ? "selected" : "" %>>In lavorazione</option>
                                	<option value="Accettata" <%= richiesta.getStato().equals(StatoRimborso.ACCETTATO) ? "selected" : "" %>>Accettato</option>
                                	<option value="Rifiutata" <%= richiesta.getStato().equals(StatoRimborso.RIFIUTATO) ? "selected" : "" %>>Rifiutato</option>
                            	</select>
                            </form>
                        </td>
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
                <h3>Non sono disponibili richieste di rimborso.</h3>
                <p>Quando verranno inviate nuove richieste, saranno disponibili qui.</p>
            </div>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
