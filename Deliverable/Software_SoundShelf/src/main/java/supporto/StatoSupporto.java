package supporto;

public enum StatoSupporto {
	IN_LAVORAZIONE("In lavorazione"),
    CHIUSA("Chiusa"),
    ATTESA_INFO("In attesa di informazioni");

    private final String stato;

    StatoSupporto(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return this.stato;
    }

    public static StatoSupporto fromString(String stato) {
        for (StatoSupporto s : StatoSupporto.values()) {
            if (s.getStato().equalsIgnoreCase(stato)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Stato non valido: " + stato);
    }
}
