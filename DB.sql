-- DROP DATABASE blok2Laadpalen;

/* CREATE DB*/
CREATE DATABASE blok2Laadpalen;
USE blok2Laadpalen;

/* CREATE WERKNEMERS TABLE*/
CREATE TABLE Werknemers (
    WerknemerId INT NOT NULL,
    Voornaam varchar(255) NOT NULL,
    Achternaam varchar(255) NOT NULL,
    Telefoonnummer varchar(255) NOT NULL UNIQUE,
    GeboorteDatum Date NOT NULL,
    PRIMARY KEY (WerknemerId)
);

/* CREATE Controles TABLE*/
CREATE TABLE Controles (
	Werknemer int NOT NULL,
	Laadpaal varchar(255) NOT NULL,
    Datum Date NOT NULL,
    Status varchar(255) default "Bezig",
    PRIMARY KEY (Laadpaal),
    FOREIGN KEY (Werknemer) REFERENCES Werknemers(WerknemerId)
);

/* CREATE INSTALLATIEPLANNINGEN TABLE*/
CREATE TABLE InstallatiePlanningen (
	Werknemer int NOT NULL,
	Laadpaal varchar(255) NOT NULL UNIQUE,
    Datum Date NOT NULL,
    PRIMARY KEY (Laadpaal),
    FOREIGN KEY (Werknemer) REFERENCES Werknemers(WerknemerId)
);

/* CREATE KLANTEN TABLE*/
CREATE TABLE Klanten (
	Telefoonnummer varchar(255) NOT NULL UNIQUE,
    Voornaam varchar(255) NOT NULL,
	Achternaam varchar(255) NOT NULL,
    PRIMARY KEY (Telefoonnummer)
);

/* CREATE RESERVERINGEN TABLE*/
CREATE TABLE Reserveringen (
	ReserveringsNummer int NOT NULL AUTO_INCREMENT,
    Klant varchar(255) NOT NULL,
	StartTijd varchar(255) NOT NULL,
    EindTijd varchar(255) NOT NULL,
    Laadpaal varchar(255) NOT NULL,
    PRIMARY KEY (ReserveringsNummer),
	FOREIGN KEY (Klant) REFERENCES Klanten(Telefoonnummer)
);

/* CONTROLE: UNIQUE CONSTRAIN FOR THE COMBINATION OF LAADPAAL AND DATUM */
ALTER TABLE Controles
  ADD CONSTRAINT uq_Controle UNIQUE(Laadpaal, Datum);
  
  /* CONTROLE: UNIQUE CONSTRAIN FOR THE COMBINATION OF LAADPAAL AND STARTTIJD */
ALTER TABLE Reserveringen
  ADD CONSTRAINT uq_Reservering UNIQUE(Laadpaal, StartTijd);