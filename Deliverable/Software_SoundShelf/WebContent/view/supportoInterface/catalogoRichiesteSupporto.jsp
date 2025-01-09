<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, supporto.*" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${pageContext.request.contextPath}/styles/style.css" rel="stylesheet" type="text/css">
    <title>Gestione Richieste di Supporto</title>
</head>
<body>
    <jsp:include page="../pagePieces/header.jsp" />
    <h1 class="page-title">Gestione Richieste di Supporto</h1>

    <%
        List<SupportRequest> richieste = (List<SupportRequest>) request.getAttribute("richieste");
        if (richieste == null || richieste.isEmpty()) {
    %>
    <p>Non ci sono richieste di supporto disponibili.</p>
    <%
        } else {
    %>
    <div class="table-container">
        <table class="requests-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Descrizione</th>
                    <th>Data Invio</th>
                    <th>Orario Invio</th>
                    <th>Stato</th>
                    <th>Cliente</th>
                    <th>Informazioni Aggiuntive</th>
                    <th>Risposta Utente</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (SupportRequest richiesta : richieste) {
                        String stato = richiesta.getStato() != null ? richiesta.getStato().getStato() : "Non disponibile";
                        String descrizione = richiesta.getDescription() != null ? richiesta.getDescription() : "Non disponibile";
                        String dataInvio = richiesta.getDataInvio() != null ? richiesta.getDataInvio().toString() : "Non disponibile";
                        String orarioInvio = richiesta.getOrarioInvio() != null ? richiesta.getOrarioInvio() : "Non disponibile";
                        int idCliente = richiesta.getIdCliente();
                        String informazioniAggiuntive = richiesta.getInformazioniAggiuntive() != null ? richiesta.getInformazioniAggiuntive() : "Non disponibile";
                        String rispostaUtente = richiesta.getRispostaUtente() != null ? richiesta.getRispostaUtente() : "Non disponibile";
                %>
                <tr>
                    <td><%= richiesta.getId() %></td>
                    <td><%= descrizione %></td>
                    <td><%= dataInvio %></td>
                    <td><%= orarioInvio %></td>
                    <td class="<%= stato.equals("Chiusa") ? "status-closed" : "" %>"><%= stato %></td>
                    <td><%= idCliente %></td>
                    <td><%= informazioniAggiuntive %></td>
                    <td><%= rispostaUtente %></td>
                </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <div class="form-container">
        <h3>Seleziona una richiesta per l'azione:</h3>
        <form action="${pageContext.request.contextPath}/gestisciRichiestaSupportoControl" method="post">
            <label for="richiestaId">Seleziona Richiesta:</label>
            <select name="richiestaId" id="richiestaId" required class="form-select">
                <option value="">-- Seleziona una richiesta --</option>
                <%
                    boolean hasInLavorazione = false;
                    for (SupportRequest richiesta : richieste) {
                        if ("In lavorazione".equals(richiesta.getStato().getStato())) {
                            hasInLavorazione = true;
                %>
                <option value="<%= richiesta.getId() %>">ID <%= richiesta.getId() %> - <%= richiesta.getDescription() %></option>
                <%
                        }
                    }
                    if (!hasInLavorazione) {
                %>
                <option value="">Nessuna richiesta in lavorazione</option>
                <%
                    }
                %>
            </select>

            <label for="azione">Seleziona Azione:</label>
            <select name="azione" id="azione" required class="form-select">
                <option value="">-- Seleziona un'azione --</option>
                <option value="richiediInformazioni">Richiedi informazioni aggiuntive</option>
                <option value="aggiornaStato">Aggiorna stato</option>
            </select>

            <!-- Contenitori nascosti per azioni -->
            <div id="informazioniAggiuntiveContainer" class="hidden">
                <label for="informazioniAggiuntive">Inserisci informazioni aggiuntive:</label>
                <textarea name="informazioniAggiuntive" id="informazioniAggiuntive" rows="5" class="form-textarea"></textarea>
            </div>

            <div id="aggiornaStatoContainer" class="hidden">
                <label for="nuovoStato">Seleziona il nuovo stato:</label>
                <select name="nuovoStato" id="nuovoStato" class="form-select">
                    <option value="In Lavorazione">In Lavorazione</option>
                    <option value="Chiusa">Chiusa</option>
                </select>
            </div>

            <button type="submit" class="button">Esegui Azione</button>
        </form>
    </div>

    <%
        }
    %>

    <jsp:include page="../pagePieces/footer.jsp" />

    <script>
        const azioneSelect = document.getElementById('azione');
        const informazioniAggiuntiveContainer = document.getElementById('informazioniAggiuntiveContainer');
        const aggiornaStatoContainer = document.getElementById('aggiornaStatoContainer');

        // Nascondi inizialmente i contenitori
        informazioniAggiuntiveContainer.style.display = 'none';
        aggiornaStatoContainer.style.display = 'none';

        azioneSelect.addEventListener('change', function() {
            if (azioneSelect.value === 'richiediInformazioni') {
                informazioniAggiuntiveContainer.style.display = 'block';
                aggiornaStatoContainer.style.display = 'none';
            } else if (azioneSelect.value === 'aggiornaStato') {
                informazioniAggiuntiveContainer.style.display = 'none';
                aggiornaStatoContainer.style.display = 'block';
            } else {
                informazioniAggiuntiveContainer.style.display = 'none';
                aggiornaStatoContainer.style.display = 'none';
            }
        });
    </script>
</body>
</html>
