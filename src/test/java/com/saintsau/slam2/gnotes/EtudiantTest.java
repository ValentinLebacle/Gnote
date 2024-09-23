package com.saintsau.slam2.gnotes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class EtudiantTest {

	@Test
	void testAjouterMatiere() {
		// Before
		HashMap<String, Matiere> matieres = new HashMap<String, Matiere>();
		Etudiant jean = new Etudiant("Jean", 1, matieres);
		Matiere info = new Matiere("Informatique", 12);
		float note = 17f;

		// Methode à tester ajouterMatiere()
		jean.ajouterMatiere(info, note);

		// Assert
		assertEquals(true, jean.getMatieres().containsValue(info));
		assertEquals(17, jean.getMatieres().get(info.getIntitule()).getNote());
	}

	@Test
	void testCalculerMoyenne() {

	}

	@Test
	void testAfficherMatieres() {
		// Before
		HashMap<String, Matiere> matieres = new HashMap<String, Matiere>();
		Matiere jeanAnglais = new Matiere("Anglais", 2, "Oral");
		matieres.put(jeanAnglais.getIntitule(), jeanAnglais);
		Matiere jeanMaths = new Matiere("Maths", 3, "DS");
		matieres.put(jeanMaths.getIntitule(), jeanMaths);
		Matiere jeanInformatiques = new Matiere("Informatiques", 3, "TP");
		matieres.put(jeanInformatiques.getIntitule(), jeanInformatiques);

		Etudiant jean = new Etudiant("Jean", 1, matieres);

		// Methode à tester afficherMatieres()
		String atester = jean.afficherMatieres();

		// Assert
		assertEquals("Matières suivies par Jean : -Anglais (Oral), -Maths (DS), -Informatiques (TP)", atester);

	}
	
	@Test
	void testMajorDePromotion() {
		//Before
		//Dimitri
		Matiere dimitrimaths = new Matiere("Maths", 3);
		dimitrimaths.setNote(12.5f);
		Matiere dimitriphysique = new Matiere("Physique", 4);
		dimitriphysique.setNote(8.5f);
		Matiere dimitrifrancais = new Matiere("Français", 2);
		dimitrifrancais.setNote(16f);
		Matiere dimitrihistoire = new Matiere("Histoire", 2);
		dimitrihistoire.setNote(12f);
		Matiere dimitrisvt = new Matiere("SVT", 3);
		dimitrisvt.setNote(10);
		
		Map<String, Matiere> matieresDimitri = new HashMap<String, Matiere>();
		matieresDimitri.put(dimitrimaths.getIntitule(),dimitrimaths);
		matieresDimitri.put(dimitriphysique.getIntitule(),dimitriphysique);
		matieresDimitri.put(dimitrifrancais.getIntitule(),dimitrifrancais);
		matieresDimitri.put(dimitrihistoire.getIntitule(),dimitrihistoire);
		matieresDimitri.put(dimitrisvt.getIntitule(),dimitrisvt);
		
		Etudiant dimitri = new Etudiant("Dimitri",1,matieresDimitri);
		
		//jean
		Matiere jeanmaths = new Matiere("Maths", 3);
		jeanmaths.setNote(8.5f);
		Matiere jeanphysique = new Matiere("Physique", 4);
		jeanphysique.setNote(14f);
		Matiere jeanfrancais = new Matiere("Français", 2);
		jeanfrancais.setNote(12f);
		Matiere jeanhistoire = new Matiere("Histoire", 2);
		jeanhistoire.setNote(16f);
		Matiere jeansvt = new Matiere("SVT", 3);
		jeansvt.setNote(12f);
		
		Map<String, Matiere> matieresJean = new HashMap<String, Matiere>();
		matieresJean.put(jeanmaths.getIntitule(),jeanmaths);
		matieresJean.put(jeanphysique.getIntitule(),jeanphysique);
		matieresJean.put(jeanfrancais.getIntitule(),jeanfrancais);
		matieresJean.put(jeanhistoire.getIntitule(),jeanhistoire);
		matieresJean.put(jeansvt.getIntitule(),jeansvt);
		
		Etudiant jean = new Etudiant("jean",1,matieresJean);
		
		//Léa
		Matiere leamaths = new Matiere("Maths", 3);
		leamaths.setNote(14f);
		Matiere leaphysique = new Matiere("Physique", 4);
		leaphysique.setNote(12f);
		Matiere leafrancais = new Matiere("Français", 2);
		leafrancais.setNote(12f);
		Matiere leahistoire = new Matiere("Histoire", 2);
		leahistoire.setNote(9f);
		Matiere leasvt = new Matiere("SVT", 3);
		leasvt.setNote(16f);
		
		Map<String, Matiere> matieresLea = new HashMap<String, Matiere>();
		matieresLea.put(leamaths.getIntitule(),leamaths);
		matieresLea.put(leaphysique.getIntitule(),leaphysique);
		matieresLea.put(leafrancais.getIntitule(),leafrancais);
		matieresLea.put(leahistoire.getIntitule(),leahistoire);
		matieresLea.put(leasvt.getIntitule(),leasvt);
		
		Etudiant lea = new Etudiant("lea",1,matieresLea);
		
		// Methode à tester majorDePromotion()
		ArrayList<Etudiant> etudiants = new ArrayList<Etudiant>();
		etudiants.add(dimitri);
		etudiants.add(lea);
		etudiants.add(jean);
		ArrayList<Etudiant> majors = Etudiant.majorDePromotion(etudiants);
		
//		System.out.println(majors.toString());
		//expect
		ArrayList<Etudiant> expected = new ArrayList<Etudiant>();
		expected.add(jean);

		// Assert
		assertEquals(jean.toString(), majors.toString());
		
	}

}
