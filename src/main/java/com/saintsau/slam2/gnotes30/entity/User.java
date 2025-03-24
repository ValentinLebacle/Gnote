package com.saintsau.slam2.gnotes30.entity;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;

    @Column(name = "user_nom")
    private String nom;

    @Column(name = "user_prenom")
    private String prenom;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "user_mail")
    private String email;

    @Column(name = "user_adresse")
    private String adresse;

    @Column(name = "user_tel")
    private String telephone;

    @OneToMany(mappedBy = "enseignant")
    @JsonIgnore
    private List<Note> notesEnseignant;

    @OneToMany(mappedBy = "eleve")
    @JsonIgnore
    private List<Note> notesEleve;

    public User() {}

    public User(String nom, String prenom, Role role, String email, String adresse, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.email = email;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getEmail() { return email; }  // Ajout du getter pour l'email
    public void setEmail(String email) { this.email = email; } // Ajout du setter pour l'email

    public String getAdresse() { return adresse; } 
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }


    // Getters et setters pour les listes de notes, si nécessaire
    public List<Note> getNotesEnseignant() { return notesEnseignant; }
    public void setNotesEnseignant(List<Note> notesEnseignant) { this.notesEnseignant = notesEnseignant; }

    public List<Note> getNotesEleve() { return notesEleve; }
    public void setNotesEleve(List<Note> notesEleve) { this.notesEleve = notesEleve; }
}
