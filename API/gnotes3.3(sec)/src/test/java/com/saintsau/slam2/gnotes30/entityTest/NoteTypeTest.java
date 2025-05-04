package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.NoteType;

import static org.junit.jupiter.api.Assertions.*;

class NoteTypeTest {

    @Test
    void testDefaultConstructor() {
        NoteType noteType = new NoteType();
        assertEquals(0, noteType.getId());
        assertNull(noteType.getLibelle());
    }

    @Test
    void testParameterizedConstructor() {
        NoteType noteType = new NoteType("Examen");
        assertEquals("Examen", noteType.getLibelle());
    }

    @Test
    void testSetAndGetId() {
        NoteType noteType = new NoteType();
        noteType.setId(5);
        assertEquals(5, noteType.getId());
    }

    @Test
    void testSetAndGetLibelle() {
        NoteType noteType = new NoteType();
        noteType.setLibelle("Contrôle continu");
        assertEquals("Contrôle continu", noteType.getLibelle());
    }
}

