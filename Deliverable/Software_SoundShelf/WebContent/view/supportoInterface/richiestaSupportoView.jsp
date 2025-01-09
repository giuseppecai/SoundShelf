<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, supporto.*"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <title>Le tue Richieste di Supporto</title>
</head>
<body>
    <jsp:include page="../pagePieces/header.jsp" />
    <h1 class="page-title">Le tue Richieste di Supporto</h1>

    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
            out.println("<p class='notification-message'>" + message + "</p>");
        }

        List<SupportRequest> richieste = (List<SupportRequest>) request.getAttribute("richieste");
        if (richieste != null && !richieste.isEmpty()) {
    %>
    <table class="requests-table">
        <thead>
            <tr>
                <th>Descrizione</th>
                <th>Data Invio</th>
                <th>Stato</th>
                <th>Informazioni Aggiuntive</th>
                <th>Risposta Utente</th>
            </tr>
        </thead>
        <tbody>
            <%
                for (SupportRequest richiesta : richieste) {
            %>
            <tr>
                <td><%= richiesta.getDescription() %></td>
                <td><%= richiesta.getDataInvio() %></td>
                <td><%= richiesta.getStato().getStato() %></td>
                <td><%= richiesta.getInformazioniAggiuntive() != null ? richiesta.getInformazioniAggiuntive() : "Nessuna" %></td>
                <td>
                    <% 
                    if (richiesta.getInformazioniAggiuntive() != null && richiesta.getRispostaUtente() == null) {
                    %>
                    <form action="${pageContext.request.contextPath}/informazioniControl" method="post" class="response-form">
                        <input type="hidden" name="idRichiesta" value="<%= richiesta.getId() %>">
                        <label for="risposta" class="response-label">La tua risposta:</label>
                        <textarea name="rispostaUtente" required class="response-textarea"></textarea>
                        <button type="submit" class="response-button">Invia Risposta</button>
                    </form>
                    <% 
                    } else if (richiesta.getRispostaUtente() != null) {
                    %>
                    <%= richiesta.getRispostaUtente() != null ? richiesta.getRispostaUtente() : "Nessuna" %>
                    <% 
                    } 
                    %>
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
        <h3>Non hai inviato richieste di supporto.</h3>
        <p>Quando invierai la tua prima richiesta, sarà visibile qui. Approfitta del nostro servizio!</p>
    </div>
    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/view/supportoInterface/helpOnlineForm.jsp" method="get" class="new-request-form">
        <button type="submit" class="new-request-button">Invia una Nuova Richiesta di Supporto</button>
    </form>

    <jsp:include page="../pagePieces/footer.jsp" />
</body>
</html>
