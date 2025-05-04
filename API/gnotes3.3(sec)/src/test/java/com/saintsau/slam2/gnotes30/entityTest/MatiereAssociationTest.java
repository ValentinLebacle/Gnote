package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;
import com.saintsau.slam2.gnotes30.entity.User;

import static org.junit.jupiter.api.Assertions.*;

class MatiereAssociationTest {

    @Test
    void testDefaultConstructor() {
        MatiereAssociation association = new MatiereAssociation();
        assertNull(association.getId());
        assertNull(association.getUser());
        assertNull(association.getMatiere());
    }

    @Test
    void testConstructorWithParams() {
        User user = new User();
        user.setId(1);
        Matiere matiere = new Matiere();
        matiere.setId(2);

        MatiereAssociation association = new MatiereAssociation(user, matiere);

        assertNotNull(association.getId());
        assertEquals(1, association.getId().getUserId());
        assertEquals(2, association.getId().getMatId());
        assertEquals(user, association.getUser());
        assertEquals(matiere, association.getMatiere());
    }

    @Test
    void testSetAndGetId() {
        MatiereAssociationId id = new MatiereAssociationId(1, 2);
        MatiereAssociation association = new MatiereAssociation();
        association.setId(id);
        assertEquals(id, association.getId());
    }

    @Test
    void testSetAndGetUser() {
        User user = new User();
        user.setId(5);
        MatiereAssociation association = new MatiereAssociation();
        association.setUser(user);
        assertEquals(user, association.getUser());
    }

    @Test
    void testSetAndGetMatiere() {
        Matiere matiere = new Matiere();
        matiere.setId(7);
        MatiereAssociation association = new MatiereAssociation();
        association.setMatiere(matiere);
        assertEquals(matiere, association.getMatiere());
    }
}
