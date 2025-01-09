<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, utente.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Profilo Utente</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
        function confirmChanges() {
            return confirm("Sei sicuro di voler modificare i tuoi dati?");
        }
    </script>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="profile-container" class="clear">
        <h2 class="profile-title">Il Tuo Profilo</h2>

        <%
        UtenteRegistrato user = (UtenteRegistrato) request.getAttribute("user");
                    if (user != null) {
        %>
        <div class="profile-info">
            <table class="profile-table">
                <tr>
                    <th>Nome:</th>
                    <td><%= user.getNome() %></td>
                </tr>
                <tr>
                    <th>Cognome:</th>
                    <td><%= user.getCognome() %></td>
                </tr>
                <tr>
                    <th>Email:</th>
                    <td><%= user.getEmail() %></td>
                </tr>
                <tr>
                    <th>Indirizzo:</th>
                    <td><%= user.getIndirizzo() %></td>
                </tr>
                <tr>
                    <th>Telefono:</th>
                    <td><%= user.getTelefono() %></td>
                </tr>
            </table>
        </div>

        <h3 class="edit-title">Modifica Dati Personali</h3>
        <form action="${pageContext.request.contextPath}/profileControl" method="post" onsubmit="return confirmChanges()" class="profile-form">
            <div class="form-group">
                <label for="nome" class="form-label">Nome:</label>
                <input type="text" id="nome" name="nome" value="<%= user.getNome() %>" required class="form-input">
            </div>

            <div class="form-group">
                <label for="cognome" class="form-label">Cognome:</label>
                <input type="text" id="cognome" name="cognome" value="<%= user.getCognome() %>" required class="form-input">
            </div>

            <div class="form-group">
                <label for="indirizzo" class="form-label">Indirizzo:</label>
                <input type="text" id="indirizzo" name="indirizzo" value="<%= user.getIndirizzo() %>" required class="form-input">
            </div>

            <div class="form-group">
                <label for="telefono" class="form-label">Telefono:</label>
                <input type="text" id="telefono" name="telefono" value="<%= user.getTelefono() %>" required class="form-input">
            </div>

            <button type="submit" class="submit-button">Salva Modifiche</button>
        </form>
        <% 
            } else {
        %>
        <p class="login-warning">Per visualizzare o modificare il tuo profilo, devi essere loggato.</p>
        <% 
            }
        %>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
