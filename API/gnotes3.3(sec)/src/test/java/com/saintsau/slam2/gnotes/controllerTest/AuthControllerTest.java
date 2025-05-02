package com.saintsau.slam2.gnotes.controllerTest;

import com.saintsau.slam2.gnotes30.controller.AuthController;
import com.saintsau.slam2.gnotes30.entity.Role;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;
import com.saintsau.slam2.gnotes30.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthControllerTest {

    private AuthService authService;
    private UserRepository userRepository;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        userRepository = mock(UserRepository.class);
        authController = new AuthController(authService, userRepository);
    }

    @Test
    void testRegisterSuccess() {
        Map<String, String> request = Map.of("email", "test@example.com", "password", "password123");

        User mockUser = new User();
        when(authService.registerUser("test@example.com", "password123")).thenReturn(mockUser);

        ResponseEntity<String> response = authController.register(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Utilisateur enregistré avec succès !", response.getBody());
    }

    @Test
    void testRegisterMissingFields() {
        Map<String, String> request = Map.of("email", "test@example.com");

        ResponseEntity<String> response = authController.register(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Email et mot de passe requis.", response.getBody());
    }

    @Test
    void testLoginSuccess() {
        Map<String, String> request = Map.of("email", "test@example.com", "password", "password123");
        String token = "mock-token";

        User mockUser = new User();
        mockUser.setId(1);  // Remplacer 1L par 1
        mockUser.setEmail("test@example.com");
        mockUser.setNom("Doe");
        mockUser.setPrenom("John");
        mockUser.setAdresse("1 rue test");
        mockUser.setTelephone("0102030405");

        Role role = new Role();
        role.setId(1);  // Remplacer 1L par 1
        role.setLibelle("ADMIN");
        mockUser.setRole(role);

        when(authService.login("test@example.com", "password123")).thenReturn(token);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertInstanceOf(Map.class, response.getBody());

        Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
        assertEquals("mock-token", responseBody.get("token"));
        assertEquals("Doe", responseBody.get("nom"));
    }


    @Test
    void testLoginInvalidCredentials() {
        Map<String, String> request = Map.of("email", "test@example.com", "password", "wrongpass");

        when(authService.login("test@example.com", "wrongpass")).thenReturn(null);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Identifiants invalides.", response.getBody());
    }

    @Test
    void testLoginUserNotFound() {
        Map<String, String> request = Map.of("email", "test@example.com", "password", "password123");

        when(authService.login("test@example.com", "password123")).thenReturn("mock-token");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Utilisateur non trouvé.", response.getBody());
    }

    @Test
    void testLoginMissingFields() {
        Map<String, String> request = Map.of("email", "test@example.com");

        when(authService.login(any(), any())).thenReturn(null);

        ResponseEntity<?> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Identifiants invalides.", response.getBody());
    }

    @Test
    void testCheckAuthValidToken() {
        when(authService.isAuthenticated("valid-token")).thenReturn(true);

        ResponseEntity<String> response = authController.checkAuth("valid-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Token valide.", response.getBody());
    }

    @Test
    void testCheckAuthInvalidToken() {
        when(authService.isAuthenticated("invalid-token")).thenReturn(false);

        ResponseEntity<String> response = authController.checkAuth("invalid-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide.", response.getBody());
    }

    @Test
    void testLogoutSuccess() {
        when(authService.logout("valid-token")).thenReturn(true);

        ResponseEntity<String> response = authController.logout("valid-token");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Déconnexion réussie.", response.getBody());
    }

    @Test
    void testLogoutFailure() {
        when(authService.logout("invalid-token")).thenReturn(false);

        ResponseEntity<String> response = authController.logout("invalid-token");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Token invalide ou déjà expiré.", response.getBody());
    }
}
