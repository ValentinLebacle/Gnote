package com.saintsau.slam2.gnotes.controllerTest;


import com.saintsau.slam2.gnotes30.controller.EnseignantController;
import com.saintsau.slam2.gnotes30.entity.Role;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EnseignantControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private EnseignantController enseignantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testGetAllEnseignants() {
        // Arrange: Créer des utilisateurs de test avec rôle ENSEIGNANT
        User user1 = new User();
        user1.setId(1);  // Utilisation d'un int au lieu d'un long
        user1.setEmail("enseignant1@example.com");
        user1.setNom("Doe");
        user1.setPrenom("John");
        Role role1 = new Role();
        role1.setLibelle("ENSEIGNANT");
        user1.setRole(role1);

        User user2 = new User();
        user2.setId(2);  // Utilisation d'un int au lieu d'un long
        user2.setEmail("enseignant2@example.com");
        user2.setNom("Smith");
        user2.setPrenom("Jane");
        Role role2 = new Role();
        role2.setLibelle("ENSEIGNANT");
        user2.setRole(role2);

        List<User> users = Arrays.asList(user1, user2);

        // Mock du service pour renvoyer une liste d'enseignants
        when(userService.getAllUsers()).thenReturn(users);

        // Act: Appel de la méthode pour récupérer tous les enseignants
        List<EntityModel<User>> response = enseignantController.getAllEtudiants();

        // Assert: Vérification du résultat
        assertEquals(2, response.size());
        assertEquals(user1.getEmail(), response.get(0).getContent().getEmail());
        assertEquals(user2.getEmail(), response.get(1).getContent().getEmail());
    }

    @Test
    void testGetAllEnseignantsEmptyList() {
        // Arrange: Liste vide d'enseignants
        when(userService.getAllUsers()).thenReturn(Arrays.asList());

        // Act: Appel de la méthode pour récupérer tous les enseignants
        List<EntityModel<User>> response = enseignantController.getAllEtudiants();

        // Assert: Vérification que la réponse est vide
        assertEquals(0, response.size());
    }

    @Test
    void testGetAllEnseignantsNoEnseignantRole() {
        // Arrange: Créer des utilisateurs avec un rôle différent (par exemple, "ETUDIANT")
        User user1 = new User();
        user1.setId(1);  // Utilisation d'un int au lieu d'un long
        user1.setEmail("etudiant1@example.com");
        user1.setNom("Dupont");
        user1.setPrenom("Pierre");
        Role role1 = new Role();
        role1.setLibelle("ETUDIANT");
        user1.setRole(role1);

        User user2 = new User();
        user2.setId(2);  // Utilisation d'un int au lieu d'un long
        user2.setEmail("etudiant2@example.com");
        user2.setNom("Martin");
        user2.setPrenom("Paul");
        Role role2 = new Role();
        role2.setLibelle("ETUDIANT");
        user2.setRole(role2);

        List<User> users = Arrays.asList(user1, user2);

        // Mock du service pour renvoyer une liste d'utilisateurs
        when(userService.getAllUsers()).thenReturn(users);

        // Act: Appel de la méthode pour récupérer tous les enseignants
        List<EntityModel<User>> response = enseignantController.getAllEtudiants();

        // Assert: Vérification que la réponse est vide (aucun enseignant)
        assertEquals(0, response.size());
    }
}
