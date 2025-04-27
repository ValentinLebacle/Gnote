package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatiereService {

	@Autowired
	private MatiereRepository matiereRepository;

	// Récupérer toutes les matières
	public List<Matiere> getAllMatieres() {
		return (List<Matiere>) matiereRepository.findAll();
	}

	// Récupérer une matière par ID
	public Optional<Matiere> getMatiereById(Integer id) {
		return matiereRepository.findById(id);
	}

	// Créer une nouvelle matière
	public Matiere createMatiere(Matiere matiere) {
		return matiereRepository.save(matiere);
	}

	public Matiere updateMatiere(Integer id, Matiere matiereDetails) {
		Matiere matiere = matiereRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Matière non trouvée avec ID " + id));
		matiere.setLibelle(matiereDetails.getLibelle());
		return matiereRepository.save(matiere);
	}

	// Supprimer une matière par ID
	public void deleteMatiere(Integer id) {
		matiereRepository.deleteById(id);
	}
}
