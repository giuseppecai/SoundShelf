DROP DATABASE IF EXISTS SoundShelf;
-- Creazione dello schema
CREATE DATABASE SoundShelf;
USE SoundShelf;

-- Tabella UtenteRegistrato
CREATE TABLE UtenteRegistrato (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    indirizzo VARCHAR(100) NOT NULL,
    telefono VARCHAR(15) NOT NULL,
    ruolo ENUM('Cliente', 'GestoreSito') NOT NULL
);

-- Tabella Artista
CREATE TABLE Artista (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cognome VARCHAR(100),
    nomeArtistico VARCHAR(100)
);

-- Tabella Genere
CREATE TABLE Genere (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Tabella Prodotto
CREATE TABLE Prodotto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descrizione VARCHAR(250) NOT NULL,
    disponibilita SMALLINT UNSIGNED,
    prezzoVendita DECIMAL(10, 2) NOT NULL,
    prezzoOriginale DECIMAL(10, 2) NOT NULL,
    formato VARCHAR(100) NOT NULL,
    immagine VARCHAR(255) NOT NULL,
    dataPubblicazione DATE NOT NULL,
    isDeleted BOOLEAN NOT NULL
);

-- Tabella Associazione Prodotto-Artista
CREATE TABLE ProdottoArtista (
    idProdotto INT NOT NULL,
    idArtista INT NOT NULL,
    FOREIGN KEY (idProdotto) REFERENCES Prodotto(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idArtista) REFERENCES Artista(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella Associazione Prodotto-Genere
CREATE TABLE ProdottoGenere (
    idProdotto INT NOT NULL,
    idGenere INT NOT NULL,
    FOREIGN KEY (idProdotto) REFERENCES Prodotto(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idGenere) REFERENCES Genere(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella Ordine
CREATE TABLE Ordine (
    numeroOrdine INT AUTO_INCREMENT PRIMARY KEY,
    dataAcquisto DATE NOT NULL,
    dataConsegna DATE NOT NULL,
    prezzoTotale DECIMAL(10, 2) NOT NULL,
    indirizzoSpedizione VARCHAR(255),
    emailCliente VARCHAR(100) NOT NULL, 
    stato ENUM('In lavorazione', 'Consegnato', 'Pagamento non ricevuto', 'Affidato al corriere', 'In attesa di pagamento', 'Annullato') NOT NULL,
    FOREIGN KEY (emailCliente) REFERENCES UtenteRegistrato(email) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella ElementoOrdine (ex OrderDetail)
CREATE TABLE ElementoOrdine (
    id INT AUTO_INCREMENT PRIMARY KEY,
    idOrdine INT NOT NULL,
    idProdotto INT NOT NULL,
    quantità SMALLINT UNSIGNED NOT NULL,
    prezzoUnitario DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (idOrdine) REFERENCES Ordine(numeroOrdine) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idProdotto) REFERENCES Prodotto(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella Recensioni
CREATE TABLE Recensioni (
    id INT AUTO_INCREMENT PRIMARY KEY,
    voto INT CHECK (voto BETWEEN 1 AND 5),
    descrizione TEXT NOT NULL,
    emailCliente VARCHAR(100) NOT NULL,
    idProdotto INT NOT NULL,
    dataRecensione DATE NOT NULL,
    FOREIGN KEY (emailCliente) REFERENCES UtenteRegistrato(email) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idProdotto) REFERENCES Prodotto(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella RichiestaSupporto
CREATE TABLE RichiestaSupporto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descrizioneRichiesta TEXT NOT NULL,
    dataInvio DATE NOT NULL,
    orarioInvio TIME NOT NULL,
    stato ENUM('In lavorazione', 'Chiusa', 'In attesa di informazioni') NOT NULL,
    informazioniAggiuntive TEXT,
    rispostaUtente TEXT,
    idCliente INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES UtenteRegistrato(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella RichiestaRimborso
CREATE TABLE RichiestaRimborso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    motivoRimborso TEXT NOT NULL,
    dataRichiesta DATE NOT NULL,
    stato ENUM('In revisione', 'Accettata', 'Rifiutata') NOT NULL,
    ibanCliente VARCHAR(27) NOT NULL,
    idOrdine INT NOT NULL,
    idProdotto INT NOT NULL,
    emailCliente VARCHAR(100) NOT NULL, 
    FOREIGN KEY (idOrdine) REFERENCES Ordine(numeroOrdine) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idProdotto) REFERENCES Prodotto(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (emailCliente) REFERENCES UtenteRegistrato(email) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabella Rimborso
CREATE TABLE Rimborso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    importoRimborso DECIMAL(10, 2) NOT NULL,
    dataEmissione DATE NOT NULL,
    idRichiesta INT NOT NULL,
    FOREIGN KEY (idRichiesta) REFERENCES RichiestaRimborso(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Inserimento nella tabella UtenteRegistrato
INSERT INTO UtenteRegistrato (nome, cognome, password, email, indirizzo, telefono, ruolo)
VALUES
('Mario', 'Rossi', 'password123', 'mario.rossi@example.com', 'Via Roma 10', '1234567890', 'Cliente'),
('Luigi', 'Bianchi', 'password456', 'luigi.bianchi@example.com', 'Via Milano 20', '1234567891', 'GestoreSito'),
('Giovanni', 'Verdi', 'password789', 'giovanni.verdi@example.com', 'Via Napoli 30', '1234567892', 'Cliente'),
('Francesca', 'Gialli', 'password101', 'francesca.gialli@example.com', 'Via Torino 40', '1234567893', 'GestoreSito'),
('Alessandro', 'Neri', 'password102', 'alessandro.neri@example.com', 'Via Venezia 50', '1234567894', 'Cliente'),
('Elena', 'Rossi', 'password103', 'elena.rossi@example.com', 'Via Firenze 60', '1234567895', 'Cliente'),
('Martina', 'Bianchi', 'password104', 'martina.bianchi@example.com', 'Via Genova 70', '1234567896', 'GestoreSito'),
('Luca', 'Gialli', 'password105', 'luca.gialli@example.com', 'Via Roma 80', '1234567897', 'Cliente'),
('Giulia', 'Verdi', 'password106', 'giulia.verdi@example.com', 'Via Milano 90', '1234567898', 'Cliente'),
('Roberto', 'Ferrari', 'password107', 'roberto.ferrari@example.com', 'Via Napoli 100', '1234567899', 'GestoreSito');

-- Inserimento nella tabella Artista
INSERT INTO Artista (nome, cognome, nomeArtistico)
VALUES
('Luciano', 'Pavarotti', 'L.Pavarotti'),
('Vasco', 'Rossi', 'Vasco Rossi'),
('Ariana', 'Grande', 'Ariana Grande'),
('The', 'Weeknd', 'The Weekend'),
('Ennio', 'Morricone', 'Ennio Morricone'),
('Shakira', 'Isabel Mebarak', 'Shakira'),
('Ed', 'Sheeran', 'Ed Sheeran'),
('Drake', 'Aubrey Graham', 'Drake'),
('Lady', 'Gaga', 'Lady Gaga'),
('Billie', 'Eilish', 'Billie Eilish');

-- Inserimento nella tabella Genere
INSERT INTO Genere (nome)
VALUES
('Pop'),
('Rock'),
('Jazz'),
('Classica'),
('Hip-Hop'),
('Elettronica'),
('R&B'),
('Soul'),
('Dance'),
('Indie');

-- Inserimento nella tabella Prodotto
INSERT INTO Prodotto (nome, descrizione, disponibilita, prezzoVendita, prezzoOriginale, formato, immagine, dataPubblicazione, isDeleted)
VALUES
('Greatest Hits', 'Le migliori canzoni di Vasco Rossi', 100, 19.99, 29.99, 'Vinile', 'image.jpg', '2022-01-01', FALSE),
('My Everything', 'Album di Ariana Grande', 200, 14.99, 24.99, 'CD', 'image.jpg', '2014-08-25', FALSE),
('Starboy', 'Album di The Weeknd', 150, 17.99, 27.99, 'Vinile', 'image.jpg', '2016-11-25', FALSE),
('Pavarotti & Friends', 'Concerto live di Luciano Pavarotti', 50, 24.99, 34.99, 'CD', 'image.jpg', '1999-09-01', FALSE),
('The Best of Shakira', 'I migliori successi di Shakira', 120, 15.99, 22.99, 'CD', 'image.jpg', '2008-11-01', FALSE),
('Shape of You', 'Ed Sheeran', 80, 18.99, 28.99, 'Vinile', 'image.jpg', '2017-03-03', FALSE),
('Lover', 'Taylor Swift', 200, 21.99, 29.99, 'CD', 'image.jpg', '2019-08-23', FALSE),
('Dark Lane Demo Tapes', 'Drake', 60, 16.99, 25.99, 'Vinile', 'image.jpg', '2020-05-01', FALSE),
('Chromatica', 'Lady Gaga', 180, 19.99, 29.99, 'CD', 'image.jpg', '2020-05-29', FALSE),
('Happier Than Ever', 'Billie Eilish', 140, 22.99, 32.99, 'Vinile', 'image.jpg', '2021-07-30', FALSE);

-- Inserimento nella tabella ProdottoArtista
INSERT INTO ProdottoArtista (idProdotto, idArtista)
VALUES
(1, 2),
(2, 3),
(3, 4),
(4, 1),
(5, 6),
(6, 7),
(7, 8),
(8, 9),
(9, 10);

-- Inserimento nella tabella ProdottoGenere
INSERT INTO ProdottoGenere (idProdotto, idGenere)
VALUES
(1, 2),
(2, 1),
(3, 1),
(4, 4),
(5, 1),
(6, 1),
(7, 2),
(8, 2),
(9, 3),
(10, 1);

-- Inserimento nella tabella Ordine
INSERT INTO Ordine (dataAcquisto, dataConsegna, prezzoTotale, indirizzoSpedizione, emailCliente, stato)
VALUES
('2024-12-01', '2024-12-05', 39.98, 'Via Roma 10', 'mario.rossi@example.com', 'In lavorazione'),
('2024-12-10', '2024-12-15', 29.99, 'Via Napoli 30', 'giovanni.verdi@example.com', 'Consegnato'),
('2024-12-12', '2024-12-20', 50.00, 'Via Milano 20', 'luigi.bianchi@example.com', 'In lavorazione'),
('2024-12-14', '2024-12-18', 30.00, 'Via Genova 70', 'martina.bianchi@example.com', 'Pagamento non ricevuto'),
('2024-12-15', '2024-12-22', 25.99, 'Via Firenze 60', 'elena.rossi@example.com', 'Consegnato'),
('2024-12-16', '2024-12-25', 15.99, 'Via Roma 80', 'luca.gialli@example.com', 'Annullato'),
('2024-12-17', '2024-12-23', 40.00, 'Via Napoli 100', 'roberto.ferrari@example.com', 'In attesa di pagamento'),
('2024-12-18', '2024-12-24', 35.99, 'Via Milano 90', 'giulia.verdi@example.com', 'In lavorazione'),
('2024-12-20', '2024-12-28', 48.50, 'Via Venezia 50', 'alessandro.neri@example.com', 'Affidato al corriere');

-- Inserimento nella tabella ElementoOrdine
INSERT INTO ElementoOrdine (idOrdine, idProdotto, quantità, prezzoUnitario)
VALUES
(1, 1, 2, 19.99),
(2, 2, 1, 14.99),
(3, 3, 3, 17.99),
(4, 4, 2, 24.99),
(5, 5, 2, 15.99),
(6, 6, 1, 18.99),
(7, 7, 1, 16.99),
(8, 8, 2, 19.99),
(9, 9, 3, 22.99);

-- Inserimento nella tabella Recensioni
INSERT INTO Recensioni (voto, descrizione, emailCliente, idProdotto, dataRecensione)
VALUES
(5, 'Album fantastico, lo consiglio a tutti!', 'mario.rossi@example.com', 1, '2024-12-01'),
(4, 'Buon album, ma mi aspettavo qualcosa di più', 'giovanni.verdi@example.com', 2, '2024-12-12'),
(3, 'Non è male, ma ci sono album migliori', 'luigi.bianchi@example.com', 3, '2024-12-14'),
(5, 'Un capolavoro della musica classica', 'francesca.gialli@example.com', 4, '2024-12-18'),
(4, 'Mi aspettavo qualcosa di più energico', 'elena.rossi@example.com', 5, '2024-12-19'),
(5, 'Bellissimo, mi fa ballare ogni volta!', 'luca.gialli@example.com', 6, '2024-12-21');

-- Inserimento nella tabella RichiestaSupporto
INSERT INTO RichiestaSupporto (descrizioneRichiesta, dataInvio, orarioInvio, stato, informazioniAggiuntive, rispostaUtente, idCliente)
VALUES
('Problema con l\'ordine, prodotto difettoso', '2024-12-05', '10:00:00', 'In lavorazione', 'Nessuna informazione aggiuntiva', NULL, 1),
('Non riesco a completare il pagamento', '2024-12-06', '11:30:00', 'Chiusa', 'Pagamento non riuscito a causa di un errore nel sistema', 'Risolto il problema, pagamento riuscito', 3),
('Non riesco a loggarmi nel sito', '2024-12-07', '14:15:00', 'In lavorazione', 'Password dimenticata', NULL, 5);

-- Inserimento dati in RichiestaRimborso
INSERT INTO RichiestaRimborso (motivoRimborso, dataRichiesta, stato, ibanCliente, idOrdine, idProdotto, emailCliente) VALUES
('Vinile rotto durante la spedizione', '2023-12-12', 'In revisione', 'IT60X0542811101000000123456', 2, 2, 'roberto.ferrari@example.com'),
('Ordine non ricevuto', '2023-11-22', 'Accettata', 'IT60X0542811101000000987654', 3, 3, 'francesca.gialli@example.com');

-- Inserimento dati in Rimborso
INSERT INTO Rimborso (importoRimborso, dataEmissione, idRichiesta) VALUES
(15.99, '2023-12-13', 1),
(12.99, '2023-11-23', 2);