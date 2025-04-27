package org.openjfx.sio2E4.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.openjfx.sio2E4.model.User;
import org.openjfx.sio2E4.service.AuthService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class UserCardController {

    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label emailLabel;
    @FXML private Label telephoneLabel;
    @FXML private Label adresseLabel;
    @FXML private Label roleLabel;
    
    private final String BEARER_TOKEN = "Bearer "+ AuthService.getToken();
    
    public void loadUser(int userId) {
        HttpClient client = HttpClient.newHttpClient();

        String urlString = "http://localhost:8080/api/users/" + userId;

        // Créer la requête avec l'authentification Bearer
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(urlString))
            .header("Authorization", BEARER_TOKEN)
            .GET()
            .build();

        // Envoyer la requête de manière asynchrone
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(this::parseUserResponse)
            .exceptionally(e -> {
                e.printStackTrace();  // Gère les exceptions liées à la requête
                return null;
            });
    }


    // Méthode pour traiter la réponse JSON et afficher les détails de l'utilisateur
    private void parseUserResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // Convertir la réponse JSON en un objet User
            User user = mapper.readValue(response, User.class);

            // Afficher les informations de l'utilisateur dans les labels
            nomLabel.setText(user.getNom());
            prenomLabel.setText(user.getPrenom());
            emailLabel.setText(user.getEmail());
            telephoneLabel.setText(user.getTelephone());
            adresseLabel.setText(user.getAdresse());
            roleLabel.setText(user.getRole().getLibelle());

        } catch (IOException e) {
            e.printStackTrace();  // Gérer l'erreur de parsing
        }
    }

}
