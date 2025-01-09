<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Invia Richiesta Supporto</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2 class="page-title">Invia una Nuova Richiesta di Supporto</h2>

        <form id="supportRequestForm" action="${pageContext.request.contextPath}/richiestaSupportoControl" method="post" onsubmit="return confirmSubmission() && validateForm()" class="support-form">
            <label for="name" class="form-label">Nome:</label>
            <input type="text" id="name" name="name" required class="form-input"><br><br>

            <label for="email" class="form-label">Email:</label>
            <input type="email" id="email" name="email" required class="form-input"><br><br>

            <label for="description" class="form-label">Descrizione:</label>
            <textarea id="description" name="description" required class="form-textarea"></textarea><br><br>

            <button type="submit" class="new-request-button">Invia Richiesta</button>
        </form>
    </div>

    <jsp:include page="../pagePieces/footer.jsp" />

    <script type="text/javascript">
        function validateForm() {
            var name = document.getElementById("name").value;
            var email = document.getElementById("email").value;
            var description = document.getElementById("description").value;
            var errorMessage = "";

            if (name.trim() === "") {
                errorMessage += "Il campo nome è obbligatorio.\n";
            }

            var emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/;
            if (!emailPattern.test(email)) {
                errorMessage += "L'email non è valida.\n";
            }

            if (description.trim() === "") {
                errorMessage += "Il campo descrizione è obbligatorio.\n";
            }

            if (errorMessage) {
                alert("Errore:\n" + errorMessage);
                return false;
            }
            return true;
        }

        function confirmSubmission() {
            return confirm("Sei sicuro di voler inviare questa richiesta di supporto?");
        }
    </script>

</body>
</html>
