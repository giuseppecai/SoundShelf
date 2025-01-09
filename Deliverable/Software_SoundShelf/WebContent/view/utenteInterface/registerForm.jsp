<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - Soundshelf</title>
    <link rel="stylesheet" href="/SoundShelf/styles/style.css" type="text/css">
    <script src="${pageContext.request.contextPath}/scripts/validation.js"></script>
    <%@ include file="../pagePieces/header.jsp" %>
</head>
<body>

<div class="container">
    <h1>Registrati su Soundshelf</h1>
    
    <form action="${pageContext.request.contextPath}/register" method="post" class="register-form">
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required placeholder="Inserisci la tua email">
        </div>
        
        <div class="form-group">
            <label for="nome">Nome</label>
            <input type="text" id="nome" name="nome" required placeholder="Inserisci il tuo nome">
        </div>
        
        <div class="form-group">
            <label for="cognome">Cognome</label>
            <input type="text" id="cognome" name="cognome" required placeholder="Inserisci il tuo cognome">
        </div>
        
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required placeholder="Crea una password">
        </div>
        
        <div class="form-group">
            <label for="via">Via</label>
            <input type="text" id="via" name="via" required placeholder="Indirizzo">
        </div>
        
        <div class="form-group">
            <label for="numero">Numero civico</label>
            <input type="text" id="numero" name="numero" required placeholder="Numero civico">
        </div>
        
        <div class="form-group">
            <label for="cap">CAP</label>
            <input type="text" id="cap" name="cap" required placeholder="CAP">
        </div>
        
        <div class="form-group">
            <label for="citta">Città</label>
            <input type="text" id="citta" name="citta" required placeholder="Città">
        </div>
        
        <div class="form-group">
            <label for="provincia">Provincia</label>
            <input type="text" id="provincia" name="provincia" required placeholder="Provincia">
        </div>
        
        <div class="form-group">
            <label for="telefono">Telefono</label>
            <input type="text" id="telefono" name="telefono" required placeholder="Numero di telefono">
        </div>

        <div class="form-group">
            <button type="submit" class="btn">Registrati</button>
        </div>
    </form>
    
    <div class="login-link">
        <p>Hai già un account? <a href="${pageContext.request.contextPath}/view/utenteInterface/loginForm.jsp">Accedi</a></p>
    </div>
</div>

<%@ include file="../pagePieces/footer.jsp" %>
</body>
</html>
