<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="ordini.Order" %>
<%@ page import="ordini.Cart" %>
<%
    session = request.getSession();
    Order order = (Order) session.getAttribute("order");
    String email = (order != null) ? order.getEmailCliente() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SoundShelf - Conferma Ordine</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
    <div>
        <jsp:include page="../pagePieces/header.jsp" />

        <section class="order-confirmation-section">
            <h1>Ordine Completato</h1>
            <p>Grazie per aver effettuato un acquisto su SoundShelf!</p>
            
            <p><strong>Totale ordine:</strong> &euro;<%= (order != null) ? order.getPrezzoTotale() : "0.00" %></p>

            <p>Abbiamo inviato una conferma d'ordine alla tua email (<%= email %>). Riceverai ulteriori istruzioni per il pagamento.</p>
            <p>Per qualsiasi domanda, non esitare a contattare il nostro servizio clienti.</p>
            
            <div class="cta-container">
                <a href="<%= request.getContextPath() %>/home" class="button">Torna alla Home</a>
            </div>
        </section>

        <jsp:include page="../pagePieces/footer.jsp" />
    </div>
</body>
</html>
