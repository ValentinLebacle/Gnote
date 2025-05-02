package com.saintsau.slam2.gnotes.controllerTest;

import com.saintsau.slam2.gnotes30.controller.EtudiantController;
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

class EtudiantControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private EtudiantController etudiantController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEtudiants() {
        // Arrange: Créer des utilisateurs de test avec rôle ETUDIANT
        User user1 = new User();
        user1.setId(1);  // Utilisation d'un int
        user1.setEmail("etudiant1@example.com");
        user1.setNom("Dupont");
        user1.setPrenom("Pierre");
        Role role1 = new Role();
        role1.setLibelle("ETUDIANT");
        user1.setRole(role1);

        User user2 = new User();
        user2.setId(2);  // Utilisation d'un int
        user2.setEmail("etudiant2@example.com");
        user2.setNom("Martin");
        user2.setPrenom("Paul");
        Role role2 = new Role();
        role2.setLibelle("ETUDIANT");
        user2.setRole(role2);

        List<User> users = Arrays.asList(user1, user2);

        // Mock du service pour renvoyer une liste d'étudiants
        when(userService.getAllUsers()).thenReturn(users);

        // Act: Appel de la méthode pour récupérer tous les étudiants
        List<EntityModel<User>> response = etudiantController.getAllEtudiants();

        // Assert: Vérification du résultat
        assertEquals(2, response.size());
        assertEquals(user1.getEmail(), response.get(0).getContent().getEmail());
        assertEquals(user2.getEmail(), response.get(1).getContent().getEmail());
    }

    @Test
    void testGetAllEtudiantsEmptyList() {
        // Arrange: Liste vide d'étudiants
        when(userService.getAllUsers()).thenReturn(Arrays.asList());

        // Act: Appel de la méthode pour récupérer tous les étudiants
        List<EntityModel<User>> response = etudiantController.getAllEtudiants();

        // Assert: Vérification que la réponse est vide
        assertEquals(0, response.size());
    }

    @Test
    void testGetAllEtudiantsNoEtudiantRole() {
        // Arrange: Créer des utilisateurs avec un rôle différent (par exemple, "ENSEIGNANT")
        User user1 = new User();
        user1.setId(1);  // Utilisation d'un int
        user1.setEmail("enseignant1@example.com");
        user1.setNom("Dupont");
        user1.setPrenom("Pierre");
        Role role1 = new Role();
        role1.setLibelle("ENSEIGNANT");
        user1.setRole(role1);

        User user2 = new User();
        user2.setId(2);  // Utilisation d'un int
        user2.setEmail("enseignant2@example.com");
        user2.setNom("Martin");
        user2.setPrenom("Paul");
        Role role2 = new Role();
        role2.setLibelle("ENSEIGNANT");
        user2.setRole(role2);

        List<User> users = Arrays.asList(user1, user2);

        // Mock du service pour renvoyer une liste d'utilisateurs
        when(userService.getAllUsers()).thenReturn(users);

        // Act: Appel de la méthode pour récupérer tous les étudiants
        List<EntityModel<User>> response = etudiantController.getAllEtudiants();

        // Assert: Vérification que la réponse est vide (aucun étudiant)
        assertEquals(0, response.size());
    }
}
