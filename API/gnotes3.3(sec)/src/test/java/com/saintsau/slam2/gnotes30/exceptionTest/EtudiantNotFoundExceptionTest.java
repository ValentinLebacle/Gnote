package com.saintsau.slam2.gnotes30.exceptionTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.exception.EtudiantNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantNotFoundExceptionTest {

    @Test
    void testExceptionMessage() {
        Long id = 123L;
        EtudiantNotFoundException exception = new EtudiantNotFoundException(id);
        
        String expectedMessage = "Étudiant avec l'ID " + id + " introuvable.";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testExceptionWithDifferentId() {
        Long id = 456L;
        EtudiantNotFoundException exception = new EtudiantNotFoundException(id);
        
        String expectedMessage = "Étudiant avec l'ID " + id + " introuvable.";
        String actualMessage = exception.getMessage();
        
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testExceptionMessageContainsId() {
        Long id = 789L;
        EtudiantNotFoundException exception = new EtudiantNotFoundException(id);
        
        // Vérifier que le message d'exception contient l'ID
        assertTrue(exception.getMessage().contains(id.toString()));
    }
}
