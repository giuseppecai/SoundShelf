<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="utente.*" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%
    session = request.getSession(false);
    boolean isAuthenticated = session != null && session.getAttribute("user") != null;
    UtenteRegistrato utente = (UtenteRegistrato) session.getAttribute("user");
%>
<header>
    <div class="nav-container">
        <a href="${pageContext.request.contextPath}/home" class="logo">SoundShelf</a>
        <ul class="nav-links">
            <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/carrelloControl">Carrello</a></li>
            <li><a href="${pageContext.request.contextPath}/view/prodottiInterface/ricercaProdottiView.jsp">Cerca</a></li>
        </ul>
        <div class="auth-buttons">
            <% if (isAuthenticated) { %>
                <ul class="nav-links">
                    <li class="profile-section">
                        <a href="#" class="profile-toggle">Profilo</a>
                        <ul class="profile-dropdown">
                            <li><a href="${pageContext.request.contextPath}/profileControl">Profilo</a></li>
                            <li><a href="${pageContext.request.contextPath}/richiestaSupportoControl">Supporto</a></li>
                            <li><a href="${pageContext.request.contextPath}/richiestaRimborsoControl">Rimborso</a></li>
                            <li><a href="${pageContext.request.contextPath}/listaOrdiniUtente">Ordini</a></li>
                            <li><a href="${pageContext.request.contextPath}/logout">LogOut</a></li>
                        </ul>
                    </li>
                    <% if (utente != null && utente.getRuolo().equals(Ruolo.GESTORESITO)) { %>
                <li class="profile-section">
                    <a href="#" class="profile-toggle">Amministrazione</a>
                    <ul class="profile-dropdown">
                        <li><a href="${pageContext.request.contextPath}/gestisciRichiesteRimborsoControl">Rimborsi</a></li>
                        <li><a href="${pageContext.request.contextPath}/gestisciCatalogoOrdiniControl">Ordini</a></li>
                        <li><a href="${pageContext.request.contextPath}/gestisciCatalogoUtentiControl">Utenti</a></li>
                        <li><a href="${pageContext.request.contextPath}/gestisciRichiestaSupportoControl">Supporto</a></li>
                        <li><a href="${pageContext.request.contextPath}/gestisciCatalogoProdottiControl">Prodotti</a></li>
                        <li><a href="${pageContext.request.contextPath}/gestisciRecensioniControl">Recensioni</a></li>
                    </ul>
                </li>
            <% } %>
                </ul>
            <% } else { %>
                <div class="user-menu">
                    <a href="${pageContext.request.contextPath}/view/utenteInterface/loginForm.jsp" class="button">Login</a>
                    <a href="${pageContext.request.contextPath}/view/utenteInterface/registerForm.jsp" class="button">Registrati</a>
                </div>
            <% } %>
        </div>
    </div>
</header>

