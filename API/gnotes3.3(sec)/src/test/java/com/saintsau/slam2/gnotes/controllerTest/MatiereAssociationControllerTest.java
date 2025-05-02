package com.saintsau.slam2.gnotes.controllerTest;

import com.saintsau.slam2.gnotes30.controller.MatiereAssociationController;
import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.MatiereAssociationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class MatiereAssociationControllerTest {

    @Mock
    private MatiereAssociationService matiereAssociationService;

    @InjectMocks
    private MatiereAssociationController matiereAssociationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAssocierMatiereAUserSuccess() {
        // Arrange
        Integer userId = 1;
        Integer matId = 2;

        User user = new User();
        user.setId(userId);

        Matiere matiere = new Matiere();
        matiere.setId(matId);

        MatiereAssociation association = new MatiereAssociation(user, matiere);

        when(matiereAssociationService.associerMatiereAUser(userId, matId))
                .thenReturn(association);

        // Act
        ResponseEntity<EntityModel<Map<String, String>>> response =
                matiereAssociationController.associerMatiereAUser(userId, matId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Matière associée avec succès !", response.getBody().getContent().get("message"));
    }
}