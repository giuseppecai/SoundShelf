package rimborsi;


public enum StatoRimborso {
	IN_REVISIONE("In revisione"),
    ACCETTATO("Accettata"),
    RIFIUTATO("Rifiutata");

    private final String stato;

    StatoRimborso(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return this.stato;
    }

    public static StatoRimborso fromString(String stato) {
        for (StatoRimborso s : StatoRimborso.values()) {
            if (s.getStato().equalsIgnoreCase(stato)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Stato non valido: " + stato);
    }
}


