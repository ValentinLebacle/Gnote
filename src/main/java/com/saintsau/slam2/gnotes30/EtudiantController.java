package com.saintsau.slam2.gnotes30;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EtudiantController {
	private final EtudiantRepository etudiantRepository;

	public EtudiantController(EtudiantRepository etudiantRepository) {
		super();
		this.etudiantRepository = etudiantRepository;
	}
	/*
	 * ETUDIANTS
	 */

	// GET localhost:8080/etudiants
	@GetMapping("/etudiants")
	List<Etudiant> all() {
		return (List<Etudiant>) etudiantRepository.findAll();
	}

	// POST localhost:8080/etudiants {"nom":"Lea"}
	@PostMapping("/etudiants")
	Etudiant newEtudiant(@RequestBody Etudiant etudiant) {
		return etudiantRepository.save(etudiant);
	}

	/*
	 * ETUDIANTS/{id}
	 */

	// GET localhost:8080/etudiants/1
	@GetMapping("/etudiants/{id}")
	Etudiant one(@PathVariable Long id) {
		Etudiant etudiant = etudiantRepository.findById(id).get();
		return etudiant;
	}

	// PUT localhost:8080/etudiants/1 {"nom":"NouveauNom"}
	@PutMapping("/etudiants/{id}")
	Etudiant updateNom(@RequestBody Etudiant updatedEtudiant, @PathVariable Long id) {
		return etudiantRepository.findById(id).map(etudiant -> {
			etudiant.setNom(updatedEtudiant.getNom()); // Met à jour uniquement le nom
			return etudiantRepository.save(etudiant); // Sauvegarde les modifications
		}).orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + id));
	}

	// DELETE localhost:8080/etudiants/1
	@DeleteMapping("/etudiants/{id}")
	void delete(@PathVariable Long id) {
		etudiantRepository.deleteById(id);
	}

	/*
	 * ETUDIANTS/{id}/MATIERES
	 */

	
	// GET localhost:8080/etudiants/{id}/matieres
	@GetMapping("/etudiants/{id}/matieres")
	List<Matiere> getMatieresByEtudiant(@PathVariable Long id) {
	    Etudiant etudiant = etudiantRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + id));
	    return new ArrayList<>(etudiant.getMatieres());
	}

	
	@PostMapping("/etudiants/{id}/matieres")
	Matiere addMatiereToEtudiant(@PathVariable Long id, @RequestBody Matiere newMatiere) {
	    return etudiantRepository.findById(id).map(etudiant -> {
	        // Associer l'étudiant à la matière
	        newMatiere.setEtudiant(etudiant);
	        // Ajouter la matière au set de matières de l'étudiant
	        etudiant.getMatieres().add(newMatiere);
	        // Sauvegarder l'étudiant avec la nouvelle matière
	        etudiantRepository.save(etudiant);
	        return newMatiere;
	    }).orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + id));
	}

	/*
	 * ETUDIANTS/{id}/MATIERES/{id}
	 */

	
	// GET localhost:8080/etudiants/{etudiantId}/matieres/{matiereId}
	@GetMapping("/etudiants/{etudiantId}/matieres/{matiereId}")
	Matiere getMatiereByEtudiant(@PathVariable Long etudiantId, @PathVariable int matiereId) {
	    Etudiant etudiant = etudiantRepository.findById(etudiantId)
	        .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + etudiantId));
	    
	    return etudiant.getMatieres().stream()
	        .filter(matiere -> matiere.getId() == matiereId)
	        .findFirst()
	        .orElseThrow(() -> new RuntimeException("Matière introuvable avec l'id " + matiereId + " pour l'étudiant " + etudiantId));
	}

	// DELETE localhost:8080/etudiants/{etudiantId}/matieres/{matiereId}
	@DeleteMapping("/etudiants/{etudiantId}/matieres/{matiereId}")
	void deleteMatiereFromEtudiant(@PathVariable Long etudiantId, @PathVariable int matiereId) {
	    Etudiant etudiant = etudiantRepository.findById(etudiantId)
	        .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + etudiantId));

	    Matiere matiereToDelete = etudiant.getMatieres().stream()
	        .filter(matiere -> matiere.getId() == matiereId)
	        .findFirst()
	        .orElseThrow(() -> new RuntimeException("Matière introuvable avec l'id " + matiereId + " pour l'étudiant " + etudiantId));

	    // Retirer la matière de l'étudiant
	    etudiant.getMatieres().remove(matiereToDelete);

	    // Sauvegarder l'étudiant pour mettre à jour la relation
	    etudiantRepository.save(etudiant);
	}

	
	// PUT localhost:8080/etudiants/{etudiantId}/matieres/{matiereId}
	@PutMapping("/etudiants/{etudiantId}/matieres/{matiereId}")
	Matiere updateMatiereForEtudiant(
	    @PathVariable Long etudiantId,
	    @PathVariable int matiereId,
	    @RequestBody Matiere updatedMatiere
	) {
	    Etudiant etudiant = etudiantRepository.findById(etudiantId)
	        .orElseThrow(() -> new RuntimeException("Étudiant introuvable avec l'id " + etudiantId));

	    Matiere matiereToUpdate = etudiant.getMatieres().stream()
	        .filter(matiere -> matiere.getId() == matiereId)
	        .findFirst()
	        .orElseThrow(() -> new RuntimeException("Matière introuvable avec l'id " + matiereId + " pour l'étudiant " + etudiantId));

	    // Mettre à jour les champs de la matière
	    matiereToUpdate.setIntitule(updatedMatiere.getIntitule());
	    matiereToUpdate.setCoef(updatedMatiere.getCoef());
	    matiereToUpdate.setType(updatedMatiere.getType());
	    matiereToUpdate.setNote(updatedMatiere.getNote());

	    // Sauvegarder les modifications de l'étudiant
	    etudiantRepository.save(etudiant);

	    return matiereToUpdate;
	}


}
