package com.saintsau.slam2.gnotes;

public class Matiere {
	String intitule;
	float note;
	int coef;
	String type;

	public Matiere(String intitule, int coef) {
		super();
		this.intitule = intitule;
		this.coef = coef;
	}

	public Matiere(String intitule, int coef, String type) {
		super();
		this.intitule = intitule;
		this.coef = coef;
		this.type = type;
	}

	public String getIntitule() {
		return intitule;
	}

	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}

	public float getNote() {
		return note;
	}

	public void setNote(float note) {
		this.note = note;
	}

	public int getCoef() {
		return coef;
	}

	public void setCoef(int coef) {
		this.coef = coef;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "La Matiere " + intitule + " de type " + type + " est de coef= " + coef;
	}

}
