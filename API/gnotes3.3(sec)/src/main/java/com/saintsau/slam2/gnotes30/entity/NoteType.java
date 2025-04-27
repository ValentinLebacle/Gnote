package com.saintsau.slam2.gnotes30.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "note_type")
public class NoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "note_type_id")
    private int id;

    @Column(name = "note_type_libelle")
    private String libelle;

    public NoteType() {}

    public NoteType(String libelle) {
        this.libelle = libelle;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
}