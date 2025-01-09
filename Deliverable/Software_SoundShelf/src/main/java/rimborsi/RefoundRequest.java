package rimborsi;

public class RefoundRequest {

    private int id;
    private String motivo;
    private String iban;
    private StatoRimborso stato;
    private int idOrdine;
    private int idProdotto;
    private String emailCliente;

    public RefoundRequest(int id, String motivo, String iban, StatoRimborso stato, int idOrdine, int idProdotto, String emailCliente) {
        this.id = id;
        this.motivo = motivo;
        this.iban = iban;
        this.stato = stato;
        this.idOrdine = idOrdine;
        this.idProdotto = idProdotto;
        this.emailCliente = emailCliente;
    }

    public RefoundRequest() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public StatoRimborso getStato() {
        return stato;
    }

    public void setStato(StatoRimborso stato) {
        this.stato = stato;
    }

    public int getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(int idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(int idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
}
