package com.saintsau.slam2.gnotes30.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matiere_association")
public class MatiereAssociation {

    @EmbeddedId
    private MatiereAssociationId id;

    @ManyToOne
    @MapsId("userId") // Fait référence à user_id dans la clé composite
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("matId") // Fait référence à mat_id dans la clé composite
    @JoinColumn(name = "mat_id")
    private Matiere matiere;

    public MatiereAssociation() {}

    public MatiereAssociation(User user, Matiere matiere) {
        this.id = new MatiereAssociationId(user.getId(), matiere.getId());
        this.user = user;
        this.matiere = matiere;
    }

    public MatiereAssociationId getId() {
        return id;
    }

    public void setId(MatiereAssociationId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }
}
