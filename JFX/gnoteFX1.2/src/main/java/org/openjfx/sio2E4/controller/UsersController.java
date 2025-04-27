package org.openjfx.sio2E4.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.openjfx.sio2E4.model.User;
import org.openjfx.sio2E4.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class UsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> nomColumn;
    @FXML private TableColumn<User, String> prenomColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> telephoneColumn;
    @FXML private TableColumn<User, String> adresseColumn;
    @FXML private TableColumn<User, String> roleColumn;

    @FXML private StackPane contentArea;  // Zone pour afficher la UserCardView

    private final String API_URL = "http://localhost:8080/api/users";
    private final String BEARER_TOKEN = "Bearer " + AuthService.getToken();

    @FXML
    public void initialize() {
        nomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNom()));
        prenomColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPrenom()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        telephoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelephone()));
        adresseColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAdresse()));
        roleColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole().getLibelle()));

        // On rend la colonne "Nom" cliquable et ajoutons un bouton pour chaque utilisateur
        nomColumn.setCellFactory(column -> {
            return new TableCell<User, String>() {
                private final Button button = new Button();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        button.setText(item);
                        button.setOnAction(event -> handleShowUserCard(getTableRow().getItem().getId()));  // Appel pour afficher la carte utilisateur
                        setGraphic(button);
                    }
                }
            };
        });

        fetchUsers();
    }

    private void fetchUsers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", BEARER_TOKEN)
                .GET()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::parseUsers)
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    private void parseUsers(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<User> users = Arrays.asList(mapper.readValue(responseBody, User[].class));
            Platform.runLater(() -> usersTable.getItems().setAll(users));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher la carte utilisateur
    private void handleShowUserCard(int userId) {
        try {
            // Charger la vue UserCardView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/openjfx/sio2E4/view/UserCardView.fxml"));
            Parent userCardRoot = loader.load();

            // Récupérer le contrôleur de UserCardView et appeler loadUser() avec l'ID
            UserCardController userCardController = loader.getController();
            userCardController.loadUser(userId);

            // Remplacer le contenu actuel du StackPane par la carte utilisateur
            contentArea.getChildren().setAll(userCardRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
