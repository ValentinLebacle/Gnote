use gnotesdb;
drop table matieres;
drop table etudiants;
drop database gnotesdb;

create database gnotesdb;
use gnotesdb;
 
CREATE TABLE `etudiants` (
  `numero` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(45) NOT NULL,
  PRIMARY KEY (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `matieres` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `intitule` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `coefficient` int(11) NOT NULL,
  `note` FLOAT(4,2) NOT NULL,
  `etudiant_numero` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_etudiant` (`etudiant_numero`),
  CONSTRAINT `fk_etudiant` FOREIGN KEY (`etudiant_numero`) REFERENCES `etudiants` (`numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;