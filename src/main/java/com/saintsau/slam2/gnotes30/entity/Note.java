package com.saintsau.slam2.gnotes30.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id_enseignant")
    private User enseignant;

    @ManyToOne
    @JoinColumn(name = "user_id_eleve")
    private User eleve;

    @ManyToOne
    @JoinColumn(name = "mat_id")
    private Matiere matiere;

    @Column(name = "note_coef")
    private BigDecimal coefficient;

    @Column(name = "note_data")
    private BigDecimal valeur;

    @ManyToOne
    @JoinColumn(name = "note_type_id")
    private NoteType noteType;

    @Column(name = "note_commentaire")
    private String commentaire;

    @Column(name = "note_date")
    @Temporal(TemporalType.DATE)
    private Date date;

    public Note() {}

    public Note(User enseignant, User eleve, Matiere matiere, BigDecimal coefficient, BigDecimal valeur, NoteType noteType, String commentaire, Date date) {
        this.enseignant = enseignant;
        this.eleve = eleve;
        this.matiere = matiere;
        this.coefficient = coefficient;
        this.valeur = valeur;
        this.noteType = noteType;
        this.commentaire = commentaire;
        this.date = date;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getEnseignant() { return enseignant; }
    public void setEnseignant(User enseignant) { this.enseignant = enseignant; }

    public User getEleve() { return eleve; }
    public void setEleve(User eleve) { this.eleve = eleve; }

    public Matiere getMatiere() { return matiere; }
    public void setMatiere(Matiere matiere) { this.matiere = matiere; }

    public BigDecimal getCoefficient() { return coefficient; }
    public void setCoefficient(BigDecimal coefficient) { this.coefficient = coefficient; }

    public BigDecimal getValeur() { return valeur; }
    public void setValeur(BigDecimal valeur) { this.valeur = valeur; }

    public NoteType getNoteType() { return noteType; }
    public void setNoteType(NoteType noteType) { this.noteType = noteType; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
}
