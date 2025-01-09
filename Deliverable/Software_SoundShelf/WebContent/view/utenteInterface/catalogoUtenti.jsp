<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.*,utente.UtenteRegistrato,utente.GestioneUtentiControl" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="ISO-8859-1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Utenti</title>
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2>Gestione Utenti</h2>

        <%
        List<UtenteRegistrato> users = (List<UtenteRegistrato>) request.getAttribute("users");
                    if (users != null && !users.isEmpty()) {
        %>
            <table class="user-table">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nome</th>
                        <th>Cognome</th>
                        <th>Ruolo</th>
                        <th>Operazioni</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    for (UtenteRegistrato user : users) {
                    %>
                    <tr>
                        <td><%= user.getEmail() %></td>
                        <td><%= user.getNome() %></td>
                        <td><%= user.getCognome() %></td>
                        <td><%= user.getRuolo() %></td>
                        <td>
                            <form action="${pageContext.request.contextPath}/gestisciCatalogoUtentiControl" method="post" style="display:inline;">
                                <input type="hidden" name="email" value="<%= user.getEmail() %>" />
                                <input type="hidden" name="action" value="delete" />
                                <button type="submit" onclick="return confirm('Sei sicuro di voler eliminare questo utente?');">Elimina</button>
                            </form>
                            <% if (!"admin".equals(user.getRuolo())) { %>
                            <form action="${pageContext.request.contextPath}/gestisciCatalogoUtentiControl" method="post" style="display:inline;">
                                <input type="hidden" name="email" value="<%= user.getEmail() %>" />
                                <input type="hidden" name="action" value="promote" />
                                <button type="submit">Promuovi a Admin</button>
                            </form>
                            <% } %>
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
            <p>Non ci sono utenti da visualizzare.</p>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
