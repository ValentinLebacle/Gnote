-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : mar. 18 mars 2025 à 09:50
-- Version du serveur : 8.0.31
-- Version de PHP : 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `newgnote`
--

DELIMITER $$
--
-- Procédures
--
DROP PROCEDURE IF EXISTS `reset`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset` ()   BEGIN
    -- Désactiver temporairement les contraintes de clé étrangère
    SET FOREIGN_KEY_CHECKS = 0;

    -- Vider les tables et réinitialiser les IDs
    TRUNCATE TABLE `newgnote`.`note`;
    TRUNCATE TABLE `newgnote`.`matiere_association`;
    TRUNCATE TABLE `newgnote`.`user`;
    TRUNCATE TABLE `newgnote`.`role`;
    TRUNCATE TABLE `newgnote`.`matiere`;
    TRUNCATE TABLE `newgnote`.`note_type`;

    -- Réactiver les contraintes de clé étrangère
    SET FOREIGN_KEY_CHECKS = 1;

    -- Réinsérer des données d'entraînement
    INSERT INTO `newgnote`.`role` (`role_id`, `role_libelle`) VALUES 
        (1, 'Administrateur'),
        (2, 'Enseignant'),
        (3, 'Élève');

    INSERT INTO `newgnote`.`user` (`user_id`, `user_nom`, `user_prenom`, `role_id`, `user_mail`, `user_adresse`, `user_tel`) VALUES 
        (1, 'Dupont', 'Jean', 2, 'jean.dupont@ecole.com', '123 Rue des Professeurs', '0123456789'),
        (2, 'Martin', 'Sophie', 3, 'sophie.martin@ecole.com', '456 Avenue des Étudiants', '0987654321'),
        (3, 'Durand', 'Paul', 3, 'paul.durand@ecole.com', '789 Boulevard des Lycéens', '0678912345');

    INSERT INTO `newgnote`.`matiere` (`mat_id`, `mat_libelle`) VALUES 
        (1, 'Mathématiques'),
        (2, 'Physique-Chimie'),
        (3, 'Histoire-Géographie');

    INSERT INTO `newgnote`.`note_type` (`note_type_id`, `note_type_libelle`) VALUES 
        (1, 'Contrôle'),
        (2, 'Examen final'),
        (3, 'Devoir maison');

    INSERT INTO `newgnote`.`note` (`user_id_enseignant`, `user_id_eleve`, `mat_id`, `note_coef`, `note_data`, `note_type_id`, `note_commentaire`, `note_date`) VALUES 
        (1, 2, 1, 2.00, 15.00, 1, 'Bon travail', '2024-02-01'),
        (1, 3, 2, 1.50, 12.50, 2, 'Peut mieux faire', '2024-02-10');

    INSERT INTO `newgnote`.`matiere_association` (`user_id`, `mat_id`) VALUES 
        (1, 1),  -- Prof enseigne les maths
        (1, 2);  -- Prof enseigne la physique-chimie

    -- Fin de procédure
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Structure de la table `matiere`
--

DROP TABLE IF EXISTS `matiere`;
CREATE TABLE IF NOT EXISTS `matiere` (
  `mat_id` int NOT NULL AUTO_INCREMENT,
  `mat_libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`mat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `matiere`
--

INSERT INTO `matiere` (`mat_id`, `mat_libelle`) VALUES
(1, 'Mathématiques'),
(2, 'Physique-Chimie'),
(3, 'Histoire-Géographie'),
(4, 'CyberSec');

-- --------------------------------------------------------

--
-- Structure de la table `note`
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE IF NOT EXISTS `note` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id_enseignant` int DEFAULT NULL,
  `user_id_eleve` int DEFAULT NULL,
  `mat_id` int DEFAULT NULL,
  `note_coef` decimal(38,2) DEFAULT NULL,
  `note_data` decimal(38,2) DEFAULT NULL,
  `note_type_id` int DEFAULT NULL,
  `note_commentaire` varchar(255) DEFAULT NULL,
  `note_date` date DEFAULT NULL,
  
  PRIMARY KEY (`id`),
  KEY `FK_user_id_enseignant_idx` (`user_id_enseignant`),
  KEY `FK_user_id_eleve_idx` (`user_id_eleve`),
  KEY `FK_user_mat_id_idx` (`mat_id`),
  KEY `FK_note_note_type_id_idx` (`note_type_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `note`
--

INSERT INTO `note` (`user_id_enseignant`, `user_id_eleve`, `mat_id`, `note_coef`, `note_data`, `note_type_id`, `note_commentaire`, `note_date`, `id`) VALUES
(1, 2, 3, '2.00', '15.50', 1, 'Très bon travail', '2024-03-17', 3);

-- --------------------------------------------------------

--
-- Structure de la table `note_type`
--

DROP TABLE IF EXISTS `note_type`;
CREATE TABLE IF NOT EXISTS `note_type` (
  `note_type_id` int NOT NULL,
  `note_type_libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`note_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `note_type`
--

INSERT INTO `note_type` (`note_type_id`, `note_type_libelle`) VALUES
(1, 'Contrôle'),
(2, 'Examen final'),
(3, 'Devoir maison');

-- --------------------------------------------------------

--
-- Structure de la table `role`
--

DROP TABLE IF EXISTS `role`;
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int NOT NULL,
  `role_libelle` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `role`
--

INSERT INTO `role` (`role_id`, `role_libelle`) VALUES
(1, 'Administrateur'),
(2, 'Enseignant'),
(3, 'Élève');

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `user_nom` varchar(255) DEFAULT NULL,
  `user_prenom` varchar(255) DEFAULT NULL,
  `role_id` int DEFAULT NULL,
  `user_mail` varchar(255) DEFAULT NULL,
  `user_adresse` varchar(255) DEFAULT NULL,
  `user_tel` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `FK_role_role_id_idx` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`user_id`, `user_nom`, `user_prenom`, `role_id`, `user_mail`, `user_adresse`, `user_tel`) VALUES
(1, 'Dupont', 'Jean', 2, 'jean.dupont@ecole.com', '123 Rue des Professeurs', '0123456789'),
(2, 'Martin', 'Sophie', 3, 'sophie.martin@ecole.com', '456 Avenue des Étudiants', '0987654321'),
(3, 'Durand', 'Paul', 3, 'paul.durand@ecole.com', '789 Boulevard des Lycéens', '0678912345');

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `note`
--
ALTER TABLE `note`
  ADD CONSTRAINT `FK_note_id_enseignant` FOREIGN KEY (`user_id_enseignant`) REFERENCES `user` (`user_id`),
  ADD CONSTRAINT `FK_note_mat_id` FOREIGN KEY (`mat_id`) REFERENCES `matiere` (`mat_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `FK_note_note_type_id` FOREIGN KEY (`note_type_id`) REFERENCES `note_type` (`note_type_id`),
  ADD CONSTRAINT `FKqs4t3nygcpmpbtqj8ivq2tbtj` FOREIGN KEY (`user_id_eleve`) REFERENCES `user` (`user_id`);

--
-- Contraintes pour la table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `FK_role_role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
