create database examen_reseaux;
use examen_reseaux;

create table log(
    mail VARCHAR(100) NOT NULL,
    password VARCHAR(50) NOT NULL
);

create table prevision(
    id INT AUTO_INCREMENT PRIMARY KEY,
    libelle VARCHAR(100),
    montant DECIMAL
);

create table depense(
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_prev INT NOT NULL,
    montant DECIMAL,
    FOREIGN KEY(id_prev) REFERENCES prevision(id)
);