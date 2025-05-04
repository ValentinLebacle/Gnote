package com.saintsau.slam2.gnotes30.entityTest;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.saintsau.slam2.gnotes30.entity.Matiere;

public class MatiereTest {

    @Test
    void testConstructeurSansParametre() {
        Matiere matiere = new Matiere();
        assertNotNull(matiere);
    }

    @Test
    void testConstructeurAvecLibelle() {
        Matiere matiere = new Matiere("Mathématiques");
        assertEquals("Mathématiques", matiere.getLibelle());
    }

    @Test
    void testSetGetId() {
        Matiere matiere = new Matiere();
        matiere.setId(10);
        assertEquals(10, matiere.getId());
    }

    @Test
    void testSetGetLibelle() {
        Matiere matiere = new Matiere();
        matiere.setLibelle("Physique");
        assertEquals("Physique", matiere.getLibelle());
    }
}
