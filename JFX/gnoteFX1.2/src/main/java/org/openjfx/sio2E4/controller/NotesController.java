package org.openjfx.sio2E4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

import org.openjfx.sio2E4.model.LocalUser;
import org.openjfx.sio2E4.model.Note;
import org.openjfx.sio2E4.service.AuthService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class NotesController {
	
	LocalUser currentUser = AuthService.getCurrentUser();
    String role = currentUser.getRole();

	
    @FXML private TableView<Note> notesTable;
    @FXML private TableColumn<Note, String> eleveColumn;
    @FXML private TableColumn<Note, String> enseignantColumn;
    @FXML private TableColumn<Note, String> matiereColumn;
    @FXML private TableColumn<Note, String> valeurColumn;
    @FXML private TableColumn<Note, String> dateColumn;
    @FXML private TableColumn<Note, String> commentaireColumn;
    @FXML private TableColumn<Note, String> noteTypeColumn;

    private final String API_URL = "http://localhost:8080/api/notes";
    private final String BEARER_TOKEN = "Bearer " + AuthService.getToken();

    @FXML
    public void initialize() {
        // Mapping des colonnes
        eleveColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getEleve().getPrenom() + " " + data.getValue().getEleve().getNom())
        );
        
        enseignantColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getEnseignant().getPrenom() + " " + data.getValue().getEnseignant().getNom())
        );
        
        matiereColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getMatiere().getLibelle())
        );
        
        valeurColumn.setCellValueFactory(data -> new SimpleStringProperty(
            String.valueOf(data.getValue().getValeur()))
        );
        
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getDate())
        );
        
        commentaireColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getCommentaire())
        );
        
        // Nouvelle colonne pour le type de la note
        noteTypeColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getNoteType())
        );

        fetchNotes();
    }

    private void fetchNotes() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(API_URL))
            .header("Authorization", BEARER_TOKEN)
            .GET()
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(this::parseNotes)
            .exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
    }

    private void parseNotes(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Note> notes = Arrays.asList(mapper.readValue(responseBody, Note[].class));
            Platform.runLater(() -> notesTable.getItems().setAll(notes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    @FXML private javafx.scene.control.TextField eleveField;
    @FXML private javafx.scene.control.TextField enseignantField;
    @FXML private javafx.scene.control.TextField matiereField;
    @FXML private javafx.scene.control.TextField valeurField;
    @FXML private javafx.scene.control.ComboBox<String> noteTypeComboBox;
    @FXML private javafx.scene.control.TextField dateField;
    @FXML private javafx.scene.control.TextField commentaireField;
    @FXML private javafx.scene.control.Button ajouterNoteButton;

    @FXML
    private void ajouterNote() {
        try {
            // Récupérer les données du formulaire
            String eleve = eleveField.getText();
            String enseignant = enseignantField.getText();
            String matiere = matiereField.getText();
            double valeur = Double.parseDouble(valeurField.getText());
            String noteType = noteTypeComboBox.getValue();
            String date = dateField.getText();
            String commentaire = commentaireField.getText();

            String json = String.format(
            	    "{"
            	    + "\"eleve\": { \"nom\": \"%s\" },"
            	    + "\"enseignant\": { \"nom\": \"%s\" },"
            	    + "\"matiere\": { \"libelle\": \"%s\" },"
            	    + "\"valeur\": %s,"
            	    + "\"noteType\": \"%s\","
            	    + "\"date\": \"%s\","
            	    + "\"commentaire\": \"%s\""
            	    + "}",
            	    eleve, enseignant, matiere, valeur, noteType, date, commentaire
            	);



            // Préparer et envoyer la requête POST
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Authorization", BEARER_TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 201 || response.statusCode() == 200) {
                        // Succès : rafraîchir la liste
                        fetchNotes();
                        clearForm();
                    } else {
                        System.err.println("Erreur à l'ajout : " + response.body());
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour vider le formulaire
    private void clearForm() {
        Platform.runLater(() -> {
            eleveField.clear();
            enseignantField.clear();
            matiereField.clear();
            valeurField.clear();
            noteTypeComboBox.setValue(null);
            dateField.clear();
            commentaireField.clear();
        });
    }
    
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
    private void handleDeleteNote() {
        Note selectedNote = notesTable.getSelectionModel().getSelectedItem();

        if (selectedNote == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une note à supprimer.");
            return;
        }

        deleteNote(selectedNote.getId());
    }

    private void deleteNote(int noteId) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/notes/" + noteId))
                .header("Authorization", "Bearer " + AuthService.getToken())
                .DELETE()
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 204) {
                        Platform.runLater(() -> {
                            showAlert(Alert.AlertType.INFORMATION, "Note supprimée avec succès.");
                            fetchNotes(); // Méthode pour recharger la liste
                        });
                    } else {
                        Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression de la note."));
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Erreur réseau : " + e.getMessage()));
                    return null;
                });
    }

}
