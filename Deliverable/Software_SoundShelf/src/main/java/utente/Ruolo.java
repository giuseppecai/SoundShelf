package utente;

public enum Ruolo {
    GESTORESITO("GestoreSito"),
    UTENTE("Cliente");

    private final String ruolo;

    Ruolo(String ruolo) {
        this.ruolo = ruolo;
    }

    public String getRuolo() {
        return ruolo;
    }

    public static Ruolo fromString(String ruolo) {
        for (Ruolo r : Ruolo.values()) {
            if (r.getRuolo().equalsIgnoreCase(ruolo)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Ruolo non valido: " + ruolo);
    }
}

