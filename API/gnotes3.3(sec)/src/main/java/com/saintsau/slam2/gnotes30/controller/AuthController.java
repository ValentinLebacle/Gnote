package com.saintsau.slam2.gnotes30.controller;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;
import com.saintsau.slam2.gnotes30.service.AuthService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }


    // Inscription d'un nouvel utilisateur
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email et mot de passe requis.");
        }

        User newUser = authService.registerUser(email, password);
        return ResponseEntity.status(HttpStatus.CREATED).body("Utilisateur enregistré avec succès !");
    }

    // Connexion et génération de token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String token = authService.login(email, password);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Identifiants invalides.");
        }

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Utilisateur non trouvé.");
        }

        User user = optionalUser.get();

        // Construction propre et ordonnée de la réponse
        Map<String, Object> response = new LinkedHashMap<>(); // <-- Utilise LinkedHashMap pour garder l’ordre

        response.put("token", token);
        response.put("id", user.getId());
        response.put("nom", user.getNom());
        response.put("prenom", user.getPrenom());
        response.put("email", user.getEmail());

        Map<String, Object> role = new LinkedHashMap<>();
        role.put("libelle", user.getRole().getLibelle());
        role.put("id", user.getRole().getId());
        response.put("role", role);

        response.put("adresse", user.getAdresse());
        response.put("telephone", user.getTelephone());

        List<Map<String, String>> links = new ArrayList<>();
        Map<String, String> selfLink = new HashMap<>();
        selfLink.put("rel", "self");
        selfLink.put("href", "http://localhost:8080/api/users/" + user.getId());
        links.add(selfLink);
        response.put("links", links);

        return ResponseEntity.ok(response);
    }



    // Vérification du token
    @GetMapping("/check")
    public ResponseEntity<String> checkAuth(@RequestHeader("Authorization") String token) {
        System.out.println("Token reçu : " + token);  // Ajouter une ligne de débogage

        // Vérifier si le token est valide
        if (authService.isAuthenticated(token)) {
            return ResponseEntity.ok("Token valide.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide.");
        }
    }
    
 // Déconnexion (Logout)
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        if (authService.logout(token)) {
            return ResponseEntity.ok("Déconnexion réussie.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalide ou déjà expiré.");
        }
    }

}
