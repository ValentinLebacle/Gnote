package com.saintsau.slam2.gnotes30;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "etudiants")
public class Etudiant {
	private String nom;
	private Long numero;
	private Set<Matiere> matieres;

	public Etudiant(String nom, Long numero, Set<Matiere> matieres) {
		super();
		this.nom = nom;
		this.numero = numero;
		this.matieres = matieres;
	}

	public Etudiant(String nom, Set<Matiere> matieres) {
		super();
		this.nom = nom;
		this.matieres = matieres;
	}
	
	public Etudiant(String nom) {
		super();
		this.nom = nom;
	}
	public Etudiant() {
		
	}

	@Column(name = "nom")
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	@Id
	@Column(name = "numero")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	@OneToMany(fetch = FetchType.EAGER,mappedBy = "etudiant", cascade = CascadeType.ALL)
	//	@JoinColumn(name = "etudiant_numero", nullable = false)
	public Set<Matiere> getMatieres() {
		return matieres;
	}

	public void setMatieres(Set<Matiere> matieres) {
		this.matieres = matieres;
	}

	public void ajouterMatiere(Matiere matiere, float note) {
		matiere.setNote(note);

		this.getMatieres().add(matiere);

	}

	@Override
	public String toString() {
		return "Etudiant [nom=" + this.nom + ", numero=" + this.numero + ", moyenne=" + this.calculerMoyenne() + "]";
	}
	
	

//	@Override
//	public int hashCode() {
//		return Objects.hash(matieres, nom, numero);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Etudiant other = (Etudiant) obj;
//		return Objects.equals(matieres, other.matieres) && Objects.equals(nom, other.nom)
//				&& Objects.equals(numero, other.numero);
//	}

	public float calculerMoyenne() {
		int sumCoef = 0;
		float sumMatiere = 0;

		for (Matiere matier : matieres) {
			float notexCoef;
			Matiere matiere = matier;
			notexCoef = matiere.getNote() * matiere.getCoef();
			sumCoef = sumCoef + matiere.getCoef();
			sumMatiere = sumMatiere + notexCoef;
		}
		// pour avoir 2 chiffres après la virgule
		return Math.round((sumMatiere / sumCoef) * 100) / 100;
	}

	public String afficherMatieres() {
		StringBuilder phrase = new StringBuilder();
		phrase.append("Matières suivies par ");
		phrase.append(this.nom);
		phrase.append(" :");
		/*
		 * Ou une autre manière simple (mais au niveau performance StringBuilder est
		 * très rapide) String pharse2 = "Matières suivies par "; pharse2 = pharse2 +
		 * this.nom + " :";
		 */
		ArrayList<String> listMatieres = new ArrayList<String>();

		for (Matiere matiere : this.matieres) {
			listMatieres.add(" -" + matiere.getIntitule() + " (" + matiere.getType() + ")");
		}

		Collections.sort(listMatieres); // N'oubliez pas d'ordonner la liste car une collection de type Set l'ordre
										// d'affichag est toujours alléatoire

		return phrase.append(listMatieres).toString();
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
