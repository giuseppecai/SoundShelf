package recensione;

import java.sql.Date;

public class Review {

    private int id;
    private int idProdotto;
    private String emailCliente;
    private int voto;
    private String descrizione;
    private Date dataRecensione;

    public Review() {
    }

    public Review(int id, int voto, String descrizione, String emailCliente, int idProdotto, Date dataRecensione) {
        this.id = id;
        this.voto = voto;
        this.descrizione = descrizione;
        this.emailCliente = emailCliente;
        this.idProdotto = idProdotto;
        this.dataRecensione = dataRecensione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getVoto() {
        return voto;
    }

    public void setVoto(int voto) {
        if(voto >= 1 && voto <= 5)
            this.voto = voto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Date getDataRecensione() {
        return dataRecensione;
    }

    public void setDataRecensione(Date dataRecensione) {
        this.dataRecensione = dataRecensione;
    }
}
