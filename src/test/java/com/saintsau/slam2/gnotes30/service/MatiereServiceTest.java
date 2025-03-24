package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatiereServiceTest {

    @Mock
    private MatiereRepository matiereRepository;

    @InjectMocks
    private MatiereService matiereService;

    private Matiere matiere;

    @BeforeEach
    void setUp() {
        // Initialisation d'une matière pour les tests
        matiere = new Matiere();
        matiere.setId(1);
        matiere.setLibelle("Mathématiques");
    }

    @Test
    void shouldReturnAllMatieres() {
        // Arrange
        when(matiereRepository.findAll()).thenReturn(List.of(matiere));

        // Act
        List<Matiere> result = matiereService.getAllMatieres();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Mathématiques", result.get(0).getLibelle());
        verify(matiereRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnMatiereById() {
        // Arrange
        when(matiereRepository.findById(1)).thenReturn(Optional.of(matiere));

        // Act
        Optional<Matiere> result = matiereService.getMatiereById(1);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Mathématiques", result.get().getLibelle());
        verify(matiereRepository, times(1)).findById(1);
    }

    @Test
    void shouldCreateMatiere() {
        // Arrange
        Matiere newMatiere = new Matiere();
        newMatiere.setLibelle("Physique");
        when(matiereRepository.save(newMatiere)).thenReturn(newMatiere);

        // Act
        Matiere savedMatiere = matiereService.createMatiere(newMatiere);

        // Assert
        assertNotNull(savedMatiere);
        assertEquals("Physique", savedMatiere.getLibelle());
        verify(matiereRepository, times(1)).save(newMatiere);
    }

    @Test
    void shouldUpdateMatiere() {
        // Arrange
        Matiere updatedMatiere = new Matiere();
        updatedMatiere.setLibelle("Informatique");

        when(matiereRepository.findById(1)).thenReturn(Optional.of(matiere));
        when(matiereRepository.save(any(Matiere.class))).thenReturn(updatedMatiere);

        // Act
        Matiere result = matiereService.updateMatiere(1, updatedMatiere);

        // Assert
        assertEquals("Informatique", result.getLibelle());
        
        // Utilisation de ArgumentCaptor pour capturer l'argument passé à save()
        ArgumentCaptor<Matiere> captor = ArgumentCaptor.forClass(Matiere.class);
        verify(matiereRepository, times(1)).save(captor.capture());

        // Vérifie que l'objet capturé correspond à l'objet attendu
        Matiere capturedMatiere = captor.getValue();
        assertEquals("Informatique", capturedMatiere.getLibelle());
        
        verify(matiereRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentMatiere() {
        // Arrange
        Matiere updatedMatiere = new Matiere();
        updatedMatiere.setLibelle("Informatique");

        when(matiereRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> matiereService.updateMatiere(1, updatedMatiere)
        );

        assertEquals("Matière non trouvée avec ID 1", exception.getMessage());
        verify(matiereRepository, times(1)).findById(1);
        verify(matiereRepository, never()).save(updatedMatiere);
    }

    @Test
    void shouldDeleteMatiere() {
        // Act
        matiereService.deleteMatiere(1);

        // Assert
        verify(matiereRepository, times(1)).deleteById(1);
    }
}
