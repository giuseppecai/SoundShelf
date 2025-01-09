package rimborsi;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ordini.ElementoOrdine;
import ordini.ElementoOrdineDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.sql.Date;

@WebServlet("/gestisciRichiesteRimborsoControl")
public class GestisciRichiestaRimborsoControl extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RefoundRequestDAO richiestaRimborsoDAO;
    private RimborsoDAO rimborsoDAO;
    private ElementoOrdineDAO elementoOrdineDAO;

    @Override
    public void init() throws ServletException {
        richiestaRimborsoDAO = new RefoundRequestDAO();
        rimborsoDAO = new RimborsoDAO();
        elementoOrdineDAO = new ElementoOrdineDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<RefoundRequest> richieste = richiestaRimborsoDAO.getAllRichiesteRimborso();
            request.setAttribute("richieste", richieste);
            request.getRequestDispatcher("view/rimborsiInterface/catalogoRichiesteRimborso.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore nel recupero delle richieste di rimborso.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        	int productId = Integer.parseInt(request.getParameter("productId"));
        	int orderID = Integer.parseInt(request.getParameter("orderId"));
        	String nuovoStato = request.getParameter("newState");
            RefoundRequest richiesta = richiestaRimborsoDAO.getRichiestaRimborso(orderID, productId);
            
                if (nuovoStato != null) {
                    try {
                        StatoRimborso stato = StatoRimborso.fromString(nuovoStato);
                        richiesta.setStato(stato);
                        richiestaRimborsoDAO.updateRichiestaRimborso(richiesta);

                        if (stato == StatoRimborso.ACCETTATO) {
                            double importoRimborso = calcolaImportoRimborso(richiesta);
                            Rimborso rimborso = new Rimborso(importoRimborso, new Date(System.currentTimeMillis()), richiesta.getId());
                            rimborsoDAO.creaRimborso(rimborso);
                        }
                    } catch (IllegalArgumentException e) {
                        request.setAttribute("errorMessage", "Stato non valido: " + nuovoStato);
                        request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
                        return;
                    }
                }
            response.sendRedirect("gestisciRichiesteRimborsoControl");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Errore durante il salvataggio delle modifiche.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore inaspettato.");
            request.getRequestDispatcher("view/error/messaggioErrore.jsp").forward(request, response);
        }
    }

    private double calcolaImportoRimborso(RefoundRequest richiesta) {
        double totalImporto = 0.0;
        List<ElementoOrdine> prodotti = elementoOrdineDAO.getOrderDetailsByOrderId(richiesta.getIdOrdine());
        for (ElementoOrdine prodotto : prodotti) {
            totalImporto += prodotto.getPrezzoUnitario() * prodotto.getQuantita();
        }
        return totalImporto;
    }

}
