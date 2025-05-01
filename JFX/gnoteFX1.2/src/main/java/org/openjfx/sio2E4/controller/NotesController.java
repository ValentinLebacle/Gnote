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
import org.openjfx.sio2E4.model.User;
import org.openjfx.sio2E4.service.AuthService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NotesController {
	
	LocalUser currentUser = AuthService.getCurrentUser();
    String role = currentUser.getRole();

    /*Tableau d'affichage de note*/
    @FXML private TableView<Note> notesTable;
    @FXML private TableColumn<Note, String> eleveColumn;
    @FXML private TableColumn<Note, String> enseignantColumn;
    @FXML private TableColumn<Note, String> matiereColumn;
    @FXML private TableColumn<Note, String> valeurColumn;
    @FXML private TableColumn<Note, String> dateColumn;
    @FXML private TableColumn<Note, String> commentaireColumn;
    @FXML private TableColumn<Note, String> noteTypeColumn;
    @FXML private TableColumn<Note, String> coefficientColumn;

    /*Formulaire de saisie de note*/
    @FXML private javafx.scene.control.ComboBox<String> eleveComboBox;
    @FXML private javafx.scene.control.ComboBox<String> enseignantComboBox;
    @FXML private javafx.scene.control.DatePicker datePicker;
    @FXML private javafx.scene.control.TextArea commentaireField;
    @FXML private javafx.scene.control.TextField coefficientField;


    
    private final String API_URL = "http://localhost:8080/api/notes";
    private final String BEARER_TOKEN = "Bearer " + AuthService.getToken();

    @FXML
    public void initialize() {
    	
    	LocalUser user = AuthService.getCurrentUser();
    	
    	String LocalUserRole = user.getRole();
		String LocalUserNom = user.getNom();
		String LocalUserPrenom = user.getPrenom();
		int LocalUserId = user.getId();
		
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
        
        coefficientColumn.setCellValueFactory(data -> new SimpleStringProperty(
        	    String.valueOf(data.getValue().getCoefficient()))
        	);


        fetchNotes();
        setupFormFields();
        chargerUtilisateursDepuisAPI();
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
            String date = datePicker.getValue().toString(); // Format YYYY-MM-DD
            double coefficient = Double.parseDouble(coefficientField.getText());

            String commentaire = commentaireField.getText();

            String json = String.format(
            	    "{"
            	    + "\"eleve\": { \"nom\": \"%s\" },"
            	    + "\"enseignant\": { \"nom\": \"%s\" },"
            	    + "\"matiere\": { \"libelle\": \"%s\" },"
            	    + "\"valeur\": %s,"
            	    + "\"coefficient\": " + coefficient
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

    private void clearForm() {
        Platform.runLater(() -> {
            eleveComboBox.setValue(null);
            enseignantComboBox.setValue(null);
            matiereField.clear();
            valeurField.clear();
            noteTypeComboBox.setValue(null);
            datePicker.setValue(null);
            commentaireField.clear();
            coefficientField.clear();
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
    
    private void setupFormFields() {
        if (role.equals("ENSEIGNANT")) {
            // Enseignant : champ auto-rempli, non éditable
            enseignantComboBox.getItems().add(currentUser.getPrenom() + " " + currentUser.getNom());
            enseignantComboBox.setValue(currentUser.getPrenom() + " " + currentUser.getNom());
            enseignantComboBox.setDisable(true); // Rendre le champ non éditable
        } else if (role.equals("ADMIN")) {
            loadEnseignants(); // Charge la liste des enseignants pour le combo
        }
        loadEleves(); // Charge les élèves pour tout le monde
    }
    
    private void chargerUtilisateursDepuisAPI() {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/api/users"))
                    .header("Authorization", BEARER_TOKEN)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            ObjectMapper mapper = new ObjectMapper();
            List<User> users = Arrays.asList(mapper.readValue(response.body(), User[].class));

            // Séparer les enseignants et les élèves par leur rôle
            List<User> enseignants = users.stream()
                    .filter(user -> user.getRole().getLibelle().equalsIgnoreCase("ENSEIGNANT"))
                    .collect(Collectors.toList());

            List<User> eleves = users.stream()
                    .filter(user -> user.getRole().getLibelle().equalsIgnoreCase("ETUDIANT"))
                    .collect(Collectors.toList());

            // Ajouter dans les ComboBox (sur le thread JavaFX)
            Platform.runLater(() -> {
                enseignantComboBox.setItems(FXCollections.observableArrayList(enseignants));
                eleveComboBox.setItems(FXCollections.observableArrayList(eleves));
            });

        } catch (Exception e) {
            e.printStackTrace(); // à remplacer par une alerte UI si besoin
        }
    }

    


}
