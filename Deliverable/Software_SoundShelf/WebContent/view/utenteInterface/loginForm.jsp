<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<jsp:include page="../pagePieces/header.jsp" />
<link rel="stylesheet" href="/SoundShelf/styles/style.css" type="text/css">
<script src="${pageContext.request.contextPath}/scripts/validation.js"></script>
<main>
    <div class="login-container">
        <h2>Login</h2>
        <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" placeholder="Inserisci la tua email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" placeholder="Inserisci la tua password" required>
            </div>
            <div class="form-actions">
                <button type="submit" class="button">Accedi</button>
            </div>
        </form>

        <p>Non hai un account? <a href="registerForm.jsp">Registrati qui</a>.</p>
    </div>
</main>
<jsp:include page="../pagePieces/footer.jsp" />
