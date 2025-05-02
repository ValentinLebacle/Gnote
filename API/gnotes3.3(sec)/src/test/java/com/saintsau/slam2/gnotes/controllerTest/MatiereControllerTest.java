package com.saintsau.slam2.gnotes.controllerTest;

import com.saintsau.slam2.gnotes30.controller.MatiereController;
import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.service.MatiereService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatiereControllerTest {

    @Mock
    private MatiereService matiereService;

    @InjectMocks
    private MatiereController matiereController;

    private Matiere matiere;

    @BeforeEach
    void setUp() {
        matiere = new Matiere();
        matiere.setId(1);
        matiere.setLibelle("Mathematics");
    }

    @Test
    void testGetAllMatieres() {
        // Arrange
        when(matiereService.getAllMatieres()).thenReturn(List.of(matiere));

        // Act
        List<EntityModel<Matiere>> result = matiereController.getAllMatieres();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(matiere.getLibelle(), result.get(0).getContent().getLibelle());
    }

    @Test
    void testGetMatiereById_MatiereExists() {
        // Arrange
        when(matiereService.getMatiereById(matiere.getId())).thenReturn(Optional.of(matiere));

        // Act
        ResponseEntity<EntityModel<Matiere>> result = matiereController.getMatiereById(matiere.getId());

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(matiere.getLibelle(), result.getBody().getContent().getLibelle());
    }

    @Test
    void testGetMatiereById_MatiereNotFound() {
        // Arrange
        when(matiereService.getMatiereById(matiere.getId())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EntityModel<Matiere>> result = matiereController.getMatiereById(matiere.getId());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testCreateMatiere() {
        // Arrange
        when(matiereService.createMatiere(matiere)).thenReturn(matiere);

        // Act
        ResponseEntity<EntityModel<Matiere>> result = matiereController.createMatiere(matiere);

        // Assert
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(matiere.getLibelle(), result.getBody().getContent().getLibelle());
    }

    @Test
    void testUpdateMatiere() {
        // Arrange
        Matiere updatedMatiere = new Matiere();
        updatedMatiere.setId(1);
        updatedMatiere.setLibelle("Updated Mathematics");
        when(matiereService.updateMatiere(matiere.getId(), updatedMatiere)).thenReturn(updatedMatiere);

        // Act
        ResponseEntity<EntityModel<Matiere>> result = matiereController.updateMatiere(matiere.getId(), updatedMatiere);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedMatiere.getLibelle(), result.getBody().getContent().getLibelle());
    }

    @Test
    void testUpdateMatiere_NotFound() {
        // Arrange
        Matiere updatedMatiere = new Matiere();
        updatedMatiere.setId(1);
        updatedMatiere.setLibelle("Updated Mathematics");
        when(matiereService.updateMatiere(matiere.getId(), updatedMatiere)).thenThrow(new RuntimeException("Matière not found"));

        // Act
        ResponseEntity<EntityModel<Matiere>> result = matiereController.updateMatiere(matiere.getId(), updatedMatiere);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }

    @Test
    void testDeleteMatiere() {
        // Act
        doNothing().when(matiereService).deleteMatiere(matiere.getId());

        // Act
        ResponseEntity<Void> result = matiereController.deleteMatiere(matiere.getId());

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());
        verify(matiereService, times(1)).deleteMatiere(matiere.getId());
    }
}
