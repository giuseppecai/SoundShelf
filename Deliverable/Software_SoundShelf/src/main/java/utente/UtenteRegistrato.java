package utente;

public class UtenteRegistrato {
	private String email;
	private String passwordUser;
	private String nome;
	private String cognome;
	private String tel;
	private String indirizzo;
	private Ruolo ruolo;

public UtenteRegistrato() {}

public UtenteRegistrato(String email, String passwordUser, String nome, String cognome, String indirizzo, String telefono, Ruolo ruolo) {
    this.email = email;
    this.passwordUser = passwordUser;
    this.nome = nome;
    this.cognome = cognome;
    this.indirizzo = indirizzo;
    this.tel = telefono;
    this.ruolo = ruolo;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getPasswordUser() {
    return passwordUser;
}

public void setPasswordUser(String passwordUser) {
    this.passwordUser = passwordUser;
}

public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}

public String getCognome() {
    return cognome;
}

public void setCognome(String cognome) {
    this.cognome = cognome;
}

public String getIndirizzo() {
    return indirizzo;
}

public void setIndirizzo(String indirizzo) {
    this.indirizzo = indirizzo;
}

public String getTelefono() {
    return tel;
}

public void setTelefono(String telefono) {
    this.tel = telefono;
}

public Ruolo getRuolo() {
    return ruolo;
}

public void setRuolo(Ruolo ruolo) {
    this.ruolo = ruolo;
}

}