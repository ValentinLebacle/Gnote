package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.NoteType;
import com.saintsau.slam2.gnotes30.entity.User;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void testDefaultConstructor() {
        Note note = new Note();
        assertNull(note.getId());
        assertNull(note.getEnseignant());
        assertNull(note.getEleve());
        assertNull(note.getMatiere());
        assertNull(note.getCoefficient());
        assertNull(note.getValeur());
        assertNull(note.getNoteType());
        assertNull(note.getCommentaire());
        assertNull(note.getDate());
    }

    @Test
    void testParameterizedConstructor() {
        User enseignant = new User();
        User eleve = new User();
        Matiere matiere = new Matiere();
        BigDecimal coefficient = new BigDecimal("2.0");
        BigDecimal valeur = new BigDecimal("15.5");
        NoteType noteType = new NoteType();
        String commentaire = "Très bien";
        Date date = new Date();

        Note note = new Note(enseignant, eleve, matiere, coefficient, valeur, noteType, commentaire, date);

        assertEquals(enseignant, note.getEnseignant());
        assertEquals(eleve, note.getEleve());
        assertEquals(matiere, note.getMatiere());
        assertEquals(coefficient, note.getCoefficient());
        assertEquals(valeur, note.getValeur());
        assertEquals(noteType, note.getNoteType());
        assertEquals(commentaire, note.getCommentaire());
        assertEquals(date, note.getDate());
    }

    @Test
    void testSetAndGetId() {
        Note note = new Note();
        note.setId(10L);
        assertEquals(10L, note.getId());
    }

    @Test
    void testSetAndGetEnseignant() {
        Note note = new Note();
        User enseignant = new User();
        note.setEnseignant(enseignant);
        assertEquals(enseignant, note.getEnseignant());
    }

    @Test
    void testSetAndGetEleve() {
        Note note = new Note();
        User eleve = new User();
        note.setEleve(eleve);
        assertEquals(eleve, note.getEleve());
    }

    @Test
    void testSetAndGetMatiere() {
        Note note = new Note();
        Matiere matiere = new Matiere();
        note.setMatiere(matiere);
        assertEquals(matiere, note.getMatiere());
    }

    @Test
    void testSetAndGetCoefficient() {
        Note note = new Note();
        BigDecimal coefficient = new BigDecimal("1.5");
        note.setCoefficient(coefficient);
        assertEquals(coefficient, note.getCoefficient());
    }

    @Test
    void testSetAndGetValeur() {
        Note note = new Note();
        BigDecimal valeur = new BigDecimal("18.0");
        note.setValeur(valeur);
        assertEquals(valeur, note.getValeur());
    }

    @Test
    void testSetAndGetNoteType() {
        Note note = new Note();
        NoteType noteType = new NoteType();
        note.setNoteType(noteType);
        assertEquals(noteType, note.getNoteType());
    }

    @Test
    void testSetAndGetCommentaire() {
        Note note = new Note();
        String commentaire = "Bon travail";
        note.setCommentaire(commentaire);
        assertEquals(commentaire, note.getCommentaire());
    }

    @Test
    void testSetAndGetDate() {
        Note note = new Note();
        Date date = new Date();
        note.setDate(date);
        assertEquals(date, note.getDate());
    }
}

