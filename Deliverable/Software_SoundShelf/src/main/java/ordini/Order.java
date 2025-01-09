package ordini;

import java.sql.Date;

public class Order {
    private int numeroOrdine;
    private String emailCliente;
    private double prezzoTotale;
    private Date dataOrdine;
    private Date dataConsegna;
    private String indirizzoSpedizione;
    private StatoOrdine stato;

    public Order() {}

    public Order(int codiceOrdine, String emailCliente, double prezzoTotale, Date dataOrdine, Date dataConsegna, String indirizzoSpedizione, StatoOrdine stato) {
        this.numeroOrdine = codiceOrdine;
        this.emailCliente = emailCliente;
        this.prezzoTotale = prezzoTotale;
        this.dataOrdine = dataOrdine;
        this.dataConsegna = dataConsegna;
        this.indirizzoSpedizione = indirizzoSpedizione;
        this.stato = stato;
    }

    public int getNumeroOrdine() {
        return numeroOrdine;
    }

    public void setNumeroOrdine(int codiceOrdine) {
        this.numeroOrdine = codiceOrdine;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public void setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public Date getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(Date dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    public StatoOrdine getStato() {
        return stato;
    }

    public void setStato(StatoOrdine stato) {
        this.stato = stato;
    }

	public Date getDataConsegna() {
		return dataConsegna;
	}

	public void setDataConsegna(Date dataConsegna) {
		this.dataConsegna = dataConsegna;
	}

	public String getIndirizzoSpedizione() {
		return indirizzoSpedizione;
	}

	public void setIndirizzoSpedizione(String indirizzoSpedizione) {
		this.indirizzoSpedizione = indirizzoSpedizione;
	}
}
