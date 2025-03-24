package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereAssociationRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.MatiereRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatiereAssociationServiceTest {

    @Mock
    private MatiereAssociationRepository matiereAssociationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatiereRepository matiereRepository;

    @InjectMocks
    private MatiereAssociationService matiereAssociationService;

    private User user;
    private Matiere matiere;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setNom("Dupont");
        user.setPrenom("Michel");

        matiere = new Matiere();
        matiere.setId(1);
        matiere.setLibelle("Mathématiques");
    }

    // Test pour associer une matière à un utilisateur
    @Test
    void shouldAssocierMatiereAUser() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(matiereRepository.findById(1)).thenReturn(Optional.of(matiere));
        
        MatiereAssociation association = new MatiereAssociation(user, matiere);
        when(matiereAssociationRepository.save(any(MatiereAssociation.class))).thenReturn(association);

        // Act
        MatiereAssociation result = matiereAssociationService.associerMatiereAUser(1, 1);

        // Assert
        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(matiere, result.getMatiere());
        verify(userRepository, times(1)).findById(1);
        verify(matiereRepository, times(1)).findById(1);
        verify(matiereAssociationRepository, times(1)).save(any(MatiereAssociation.class));
    }

    // Test pour supprimer une association entre un utilisateur et une matière
    @Test
    void shouldSupprimerAssociation() {
        // Arrange
        MatiereAssociationId id = new MatiereAssociationId(1, 1);
        MatiereAssociation association = new MatiereAssociation();
        association.setId(id);

        when(matiereAssociationRepository.findById(id)).thenReturn(Optional.of(association));

        // Act
        matiereAssociationService.supprimerAssociation(1, 1);

        // Assert
        verify(matiereAssociationRepository, times(1)).findById(id);
        verify(matiereAssociationRepository, times(1)).delete(association);
    }

    // Test pour vérifier si une exception est lancée lorsqu'on essaie de supprimer une association qui n'existe pas
    @Test
    void shouldThrowExceptionWhenSupprimerAssociationNotFound() {
        // Arrange
        MatiereAssociationId id = new MatiereAssociationId(1, 1);
        when(matiereAssociationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            matiereAssociationService.supprimerAssociation(1, 1);
        });

        assertEquals("L'association n'existe pas", exception.getMessage());
        verify(matiereAssociationRepository, times(1)).findById(id);
        verify(matiereAssociationRepository, never()).delete(any());
    }
}
