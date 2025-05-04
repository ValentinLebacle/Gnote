package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;

import static org.junit.jupiter.api.Assertions.*;

class MatiereAssociationIdTest {

    @Test
    void testDefaultConstructor() {
        MatiereAssociationId id = new MatiereAssociationId();
        assertNull(id.getUserId());
        assertNull(id.getMatId());
    }

    @Test
    void testConstructorWithParams() {
        MatiereAssociationId id = new MatiereAssociationId(1, 2);
        assertEquals(1, id.getUserId());
        assertEquals(2, id.getMatId());
    }

    @Test
    void testSetAndGetUserId() {
        MatiereAssociationId id = new MatiereAssociationId();
        id.setUserId(5);
        assertEquals(5, id.getUserId());
    }

    @Test
    void testSetAndGetMatId() {
        MatiereAssociationId id = new MatiereAssociationId();
        id.setMatId(8);
        assertEquals(8, id.getMatId());
    }

    @Test
    void testEqualsAndHashCode_sameValues() {
        MatiereAssociationId id1 = new MatiereAssociationId(1, 2);
        MatiereAssociationId id2 = new MatiereAssociationId(1, 2);

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }

    @Test
    void testEquals_differentValues() {
        MatiereAssociationId id1 = new MatiereAssociationId(1, 2);
        MatiereAssociationId id2 = new MatiereAssociationId(3, 4);

        assertNotEquals(id1, id2);
    }

    @Test
    void testEquals_withNullAndOtherObjectTypes() {
        MatiereAssociationId id = new MatiereAssociationId(1, 2);

        assertNotEquals(null, id);
        assertNotEquals("notAnId", id);
    }

    @Test
    void testHashCode_consistency() {
        MatiereAssociationId id = new MatiereAssociationId(1, 2);
        int hash1 = id.hashCode();
        int hash2 = id.hashCode();
        assertEquals(hash1, hash2);
    }
}
