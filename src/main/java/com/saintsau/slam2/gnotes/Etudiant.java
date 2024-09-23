package com.saintsau.slam2.gnotes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Etudiant {
	String nom;
	int numero;
	Map<String, Matiere> matieres;

	public Etudiant(String nom, int numero, Map<String, Matiere> matieres) {
		super();
		this.nom = nom;
		this.numero = numero;
		this.matieres = matieres;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Map<String, Matiere> getMatieres() {
		return matieres;
	}

	public void setMatieres(Map<String, Matiere> matieres) {
		this.matieres = matieres;
	}

	public void ajouterMatiere(Matiere matiere, float note) {
		matiere.setNote(note);
		if (!this.getMatieres().containsValue(matiere)) {
			this.getMatieres().put(matiere.getIntitule(), matiere);
		}
	}

	@Override
	public String toString() {
		return "Etudiant [nom=" + nom + ", numero=" + numero + ", matieres=" + matieres + "]. Sa moyenne est de : "
				+ this.calculerMoyenne();
	}

	public float calculerMoyenne() {
		int sumCoef = 0;
		float sumMatiere = 0;

		for (String key : matieres.keySet()) {
			float notexCoef;
			Matiere matiere = matieres.get(key);
			notexCoef = matiere.getNote() * matiere.getCoef();
			sumCoef = sumCoef + matiere.getCoef();
			sumMatiere = sumMatiere + notexCoef;
		}

//		float moyenneReel = sumMatiere / sumCoef;
//		//Convertir le float en BigDEcimal pour une meilleure précision
//		BigDecimal bd = new BigDecimal(Float.toString(moyenneReel));
//		BigDecimal partieEntiere = bd.setScale(0,RoundingMode.DOWN);
//		BigDecimal partieDecimale = bd.subtract(partieEntiere);

		// pour avoir 2 chiffres après la virgule
		return Math.round((sumMatiere / sumCoef) * 100) / 100;
	}

	public String afficherMatieres() {
		StringBuilder phrase = new StringBuilder();
		phrase.append("Matières suivies par ");
		phrase.append(nom);
		phrase.append(" :");

		for (String key : matieres.keySet()) {
			Matiere matiere = matieres.get(key);
			phrase.append(" -");
			phrase.append(matiere.getIntitule());
			phrase.append(" (");
			phrase.append(matiere.getType());
			phrase.append("),");
		}

		return phrase.substring(0, phrase.length() - 1);
	}

	public static ArrayList<Etudiant> majorDePromotion(List<Etudiant> etudiants) {
		float moyenneMajor = 0;
		ArrayList<Etudiant> majors = new ArrayList<Etudiant>();
		for (Etudiant etudiant : etudiants) {
			float moyenneEtudiant = etudiant.calculerMoyenne();

			if (moyenneMajor < moyenneEtudiant) {
				majors.clear();
				majors.add(etudiant);
			} else if (moyenneMajor == moyenneEtudiant) {
				majors.add(etudiant);
			}
		}

		return majors;
	}
}
