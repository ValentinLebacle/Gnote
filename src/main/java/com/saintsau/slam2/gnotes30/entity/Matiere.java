package com.saintsau.slam2.gnotes30.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mat_id")
    private int id;

    @Column(name = "mat_libelle")
    private String libelle;

    @OneToMany(mappedBy = "matiere")
    private List<MatiereAssociation> associations;

    @OneToMany(mappedBy = "matiere")
    private List<Note> notes;

    public Matiere() {}

    public Matiere(String libelle) {
        this.libelle = libelle;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}
