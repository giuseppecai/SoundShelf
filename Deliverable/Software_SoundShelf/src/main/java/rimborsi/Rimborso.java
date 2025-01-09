package rimborsi;

import java.sql.Date;

public class Rimborso {
    private int id;
    private double importoRimborso;
    private Date dataEmissione;
    private int idRichiesta;

    public Rimborso(int id, double importoRimborso, Date dataEmissione, int idRichiesta) {
        this.id = id;
        this.importoRimborso = importoRimborso;
        this.dataEmissione = dataEmissione;
        this.idRichiesta = idRichiesta;
    }
    
    public Rimborso(double importoRimborso, Date dataEmissione, int idRichiesta) {
    	this.importoRimborso = importoRimborso;
        this.dataEmissione = dataEmissione;
        this.idRichiesta = idRichiesta;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getImportoRimborso() {
        return importoRimborso;
    }

    public void setImportoRimborso(double importoRimborso) {
        this.importoRimborso = importoRimborso;
    }

    public Date getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(Date dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public int getIdRichiesta() {
        return idRichiesta;
    }

    public void setIdRichiesta(int idRichiesta) {
        this.idRichiesta = idRichiesta;
    }
}
