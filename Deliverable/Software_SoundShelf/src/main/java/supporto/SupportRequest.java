package supporto;

import java.sql.Date;

public class SupportRequest {

    private int id;
    private String description;
    private Date dataInvio;
    private String orarioInvio;
    private StatoSupporto stato;
    private String informazioniAggiuntive;
    private String rispostaUtente;
    private int idCliente;

    public SupportRequest(int id, String description, Date dataInvio, String orarioInvio, StatoSupporto stato, 
                          String informazioniAggiuntive, String rispostaUtente, int idCliente) {
        this.id = id;
        this.description = description;
        this.dataInvio = dataInvio;
        this.orarioInvio = orarioInvio;
        this.stato = stato;
        this.informazioniAggiuntive = informazioniAggiuntive;
        this.rispostaUtente = rispostaUtente;
        this.idCliente = idCliente;
    }

    public SupportRequest(int id, String description, Date dataInvio, String orarioInvio, StatoSupporto stato, 
                          String informazioniAggiuntive, int idCliente) {
        this(id, description, dataInvio, orarioInvio, stato, informazioniAggiuntive, null, idCliente);
    }

    public SupportRequest(String description, Date dataInvio, String orarioInvio, StatoSupporto stato, int idCliente) {
        this(0, description, dataInvio, orarioInvio, stato, null, null, idCliente);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDataInvio() {
        return dataInvio;
    }

    public void setDataInvio(Date dataInvio) {
        this.dataInvio = dataInvio;
    }

    public String getOrarioInvio() {
        return orarioInvio;
    }

    public void setOrarioInvio(String orarioInvio) {
        this.orarioInvio = orarioInvio;
    }

    public StatoSupporto getStato() {
        return stato;
    }

    public void setStato(StatoSupporto stato) {
        this.stato = stato;
    }

    public String getInformazioniAggiuntive() {
        return informazioniAggiuntive;
    }

    public void setInformazioniAggiuntive(String informazioniAggiuntive) {
        this.informazioniAggiuntive = informazioniAggiuntive;
    }

    public String getRispostaUtente() {
        return rispostaUtente;
    }

    public void setRispostaUtente(String rispostaUtente) {
        this.rispostaUtente = rispostaUtente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
