<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="ordini.*, prodotti.*"%>
<%@ page import="java.util.List"%>
<%
    List<Order> orders = (List<Order>) request.getAttribute("ordini");
    ElementoOrdineDAO orderDetailDAO = new ElementoOrdineDAO();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>I tuoi ordini</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/style.css">
</head>
<body>
    <div>
        <jsp:include page="../pagePieces/header.jsp" />
        <section class="orders-section">
            <h2>I Tuoi Ordini</h2>
            <div class="order-list">
                <%
                if (orders != null && !orders.isEmpty()) {
                    for (Order order : orders) {
                %>
                <div class="order-card">
                    <h3>Ordine <%= order.getNumeroOrdine() %></h3>
                    <table class="order-table">
                        <tr>
                            <th>Codice Ordine</th>
                            <td><%= order.getNumeroOrdine() %></td>
                        </tr>
                        <tr>
                            <th>Data</th>
                            <td><%= order.getDataOrdine() %></td>
                        </tr>
                        <tr>
                            <th>Totale</th>
                            <td>&euro;<%= order.getPrezzoTotale() %></td>
                        </tr>
                        <tr>
                            <th>Stato</th>
                            <td><%= order.getStato().getStato() %></td>
                        </tr>
                    </table>
                    <h4>Dettagli Ordine</h4>
                    <table class="order-table">
                        <tr>
                            <th>Prodotto</th>
                            <th>Quantità</th>
                            <th>Prezzo</th>
                            <th>Azioni</th>
                        </tr>
                        <%
                        List<ElementoOrdine> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getNumeroOrdine());
                        for (ElementoOrdine detail : orderDetails) {
                            Product product = (Product) request.getAttribute("prodotto_" + detail.getIdProdotto());
                        %>
                        <tr>
                            <td><%= product != null ? product.getName() : "Prodotto non disponibile" %></td>
                            <td><%= detail.getQuantita() %></td>
                            <td>&euro;<%= product != null ? product.getSalePrice() * detail.getQuantita() : 0 %></td>
                            <td>
                             	<%
                                if ("Consegnato".equals(order.getStato().getStato())) {
                                %>
                                <form action="${pageContext.request.contextPath}/inviaRichiestaRimborsoControl" method="get">
                                    <input type="hidden" name="detailCode" value="<%= detail.getId() %>">
                                    <button type="submit" class="refund-button">Richiedi Rimborso</button>
                                </form>
                                <form action="${pageContext.request.contextPath}/addReview" method="get">
                                    <input type="hidden" name="productCode" value="<%= detail.getIdProdotto() %>">
                                    <button type="submit" class="refund-button">Lascia una recensione</button>
                                </form>
                                <%
                                }
                                %>
                            </td>
                        </tr>
                        <%
                        }
                        %>
                    </table>
                    <%
                    if ("Affidato al corriere".equals(order.getStato().getStato())) {
                    %>
                    <form action="${pageContext.request.contextPath}/OrdineRicevutoControl" method="post">
                        <input type="hidden" name="ordineId" value="<%= order.getNumeroOrdine() %>">
                        <input type="hidden" name="confermaRicezione" value="si">
                        <button type="submit" class="confirm-receipt-button">Conferma Ricezione</button>
                    </form>
                    <%
                    }
                    %>
                </div>
                <%
                    }
                } else {
                %>
                <div class="order-card empty-order">
                    <h3>Non hai ancora effettuato ordini.</h3>
                    <p>Quando effettuerai il primo ordine, sarà visibile qui. Approfitta delle nostre offerte!</p>
                </div>
                <%
                }
                %>
            </div>
        </section>
        <jsp:include page="../pagePieces/footer.jsp" />
    </div>
</body>
</html>
