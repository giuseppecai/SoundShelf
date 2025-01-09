<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, ordini.*, rimborsi.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Richiesta Rimborso</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
        <script>
        function confirmRefund() {
            return confirm("Sei sicuro di voler richiedere un rimborso per questo prodotto?");
        }
    </script>
</head>
<body>

    <jsp:include page="../pagePieces/header.jsp" />

    <div id="main" class="clear">
        <h2 class="page-title">Dettaglio Rimborso</h2>

        <% 
            ElementoOrdine elemento = (ElementoOrdine) request.getAttribute("orderDetail");
            int quantita = elemento.getQuantita();
            double totale = quantita * elemento.getPrezzoUnitario();
            if (elemento != null) {
        %>

            <form action="${pageContext.request.contextPath}/inviaRichiestaRimborsoControl" method="post" onsubmit="return confirmRefund();" class="refund-form">
            	<label for="orderDetailID" class="form-label">Codice prodotto da rendere:</label>
                <input type="text" id="orderDetailID" name="orderDetailID" value="<%= elemento.getIdProdotto() %>" readonly class="form-input"/><br><br>

                <label for="reason" class="form-label">Inserisci il motivo del rimborso:</label>
                <textarea id="reason" name="reason" required class="form-textarea"></textarea><br><br>

                <label for="iban" class="form-label">Inserisci il tuo IBAN:</label>
                <input type="text" id="iban" name="iban" required class="form-input"><br><br>

                <button type="submit" class="refund-button" onsubmit="return confirmRefund();">Invia Richiesta</button>
            </form>
        <% 
            }
        %>
        <br>
    </div>
    <script>
        function confirmRefund() {
            return confirm("Sei sicuro di voler richiedere il rimborso per questo prodotto?");
        }
    </script>
    <jsp:include page="../pagePieces/footer.jsp" />

</body>
</html>
