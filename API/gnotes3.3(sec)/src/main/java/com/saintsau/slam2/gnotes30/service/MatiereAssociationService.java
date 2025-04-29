package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereAssociationRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatiereAssociationService {

	@Autowired
	private MatiereAssociationRepository matiereAssociationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MatiereRepository matiereRepository;

	// Méthode pour associer une matière à un utilisateur
	public MatiereAssociation associerMatiereAUser(Integer userId, Integer matId) {
		// Vérifier si l'utilisateur existe
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

		// Vérifier si la matière existe
		Matiere matiere = matiereRepository.findById(matId)
				.orElseThrow(() -> new RuntimeException("Matière non trouvée"));

		// Créer une nouvelle association
		MatiereAssociation association = new MatiereAssociation(user, matiere);

		// Sauvegarder l'association
		return matiereAssociationRepository.save(association);
	}

	// Méthode pour supprimer une association entre un utilisateur et une matière
	public void supprimerAssociation(Integer userId, Integer matId) {
		MatiereAssociation association = matiereAssociationRepository.findById(new MatiereAssociationId(userId, matId))
				.orElseThrow(() -> new RuntimeException("L'association n'existe pas"));

		matiereAssociationRepository.delete(association);
	}
	
	
	
	

	public List<Matiere> getMatieresByUser(Integer userId) {
	    List<MatiereAssociation> associations = matiereAssociationRepository.findByUserId(userId);

	    if (associations.isEmpty()) {
	        throw new RuntimeException("Aucune matière trouvée pour cet utilisateur");
	    }

	    return associations.stream()
	            .map(MatiereAssociation::getMatiere)
	            .collect(Collectors.toList());
	}

	
}
