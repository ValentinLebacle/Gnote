package org.openjfx.sio2E4.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;

import org.openjfx.sio2E4.model.LocalUser;
import org.openjfx.sio2E4.model.Matiere;
import org.openjfx.sio2E4.model.Note;
import org.openjfx.sio2E4.model.NoteType;
import org.openjfx.sio2E4.model.User;
import org.openjfx.sio2E4.service.AuthService;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.control.ListCell;

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


    private final String API_URL = "http://localhost:8080/api/notes";
    private final String BEARER_TOKEN = "Bearer " + AuthService.getToken();

    
    private void showAlert(AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void clearForm() {
        Platform.runLater(() -> {
            eleveComboBox.setValue(null);
            enseignantComboBox.setValue(null);
            matiereComboBox.setValue(null);
            valeurField.clear();
            noteTypeComboBox.setValue(null);
            datePicker.setValue(null);
            commentaireField.clear();
            coefficientField.clear();
        });
    }
    
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
        
        coefficientColumn.setCellValueFactory(data -> new SimpleStringProperty(
        	    String.valueOf(data.getValue().getCoefficient()))
        	);

        /*----FORMATTAGE DES COMBOBOX----*/
       
        noteTypeComboBox.setCellFactory(lv -> new ListCell<NoteType>() {
            @Override
            protected void updateItem(NoteType item, boolean empty) {
                super.updateItem(item, empty);
                // Affiche le libellé ou "vide" si l'élément est null ou la cellule vide
                setText(empty || item == null ? null : item.getLibelle());
            }
        });
        noteTypeComboBox.setButtonCell(noteTypeComboBox.getCellFactory().call(null)); // Rendu du bouton du ComboBox

        eleveComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPrenom() + " " + item.getNom());
            }
        });
        eleveComboBox.setButtonCell(eleveComboBox.getCellFactory().call(null)); // Rendu du bouton du ComboBox


        enseignantComboBox.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getPrenom() + " " + item.getNom());
            }
        });
        enseignantComboBox.setButtonCell(enseignantComboBox.getCellFactory().call(null)); // Rendu du bouton du ComboBox

     // Chargement des données du tableaus
        fetchNotes();
        
     // Chargement des données du formulaire
        fetchUsers();
        fetchMatieres();
        fetchNoteTypes();

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
    


    /*Formulaire de saisie de note*/
    @FXML private javafx.scene.control.TextField valeurField;
    @FXML private javafx.scene.control.TextField coefficientField;
    @FXML private javafx.scene.control.TextArea commentaireField;
    
    @FXML private javafx.scene.control.ComboBox<User> eleveComboBox;
    @FXML private javafx.scene.control.ComboBox<User> enseignantComboBox;
    @FXML private javafx.scene.control.ComboBox<Matiere> matiereComboBox;
    @FXML private javafx.scene.control.ComboBox<NoteType> noteTypeComboBox;
    
    @FXML private javafx.scene.control.DatePicker datePicker;
    
    @FXML private javafx.scene.control.Button ajouterNoteButton;


    @FXML
    private void ajouterNote() {
        try {
            // Récupérer les données du formulaire
        	User eleve = eleveComboBox.getValue();
        	User enseignant = enseignantComboBox.getValue();
        	Matiere matiere = matiereComboBox.getValue();
        	NoteType noteType = noteTypeComboBox.getValue();

        	double valeur = Double.parseDouble(valeurField.getText());
        	double coefficient = Double.parseDouble(coefficientField.getText());
        	String date = datePicker.getValue().toString();
        	String commentaire = commentaireField.getText();

        	String json = String.format(
        	    "{"
        	        + "\"eleve\": { \"id\": %d },"
        	        + "\"enseignant\": { \"id\": %d },"
        	        + "\"matiere\": { \"id\": %d },"
        	        + "\"coefficient\": %s,"
        	        + "\"valeur\": %s,"
        	        + "\"noteType\": { \"id\": %d },"
        	        + "\"commentaire\": \"%s\","
        	        + "\"date\": \"%s\""
        	    + "}",
        	    eleve.getId(), enseignant.getId(), matiere.getId(),
        	    coefficient, valeur, noteType.getId(),
        	    commentaire, date
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
    
    
    
    private void fetchUsers() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/users"))
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
        LocalUser user = AuthService.getCurrentUser();
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<User> users = Arrays.asList(mapper.readValue(responseBody, User[].class));

            // Filtrer les utilisateurs par rôle
            List<User> eleves = users.stream()
                .filter(u -> "ETUDIANT".equalsIgnoreCase(u.getRole().getLibelle()))
                .collect(Collectors.toList());

            List<User> enseignants = users.stream()
                .filter(u -> "ENSEIGNANT".equalsIgnoreCase(u.getRole().getLibelle()))
                .collect(Collectors.toList());

            // Mettre à jour les ComboBox dans le thread JavaFX
            Platform.runLater(() -> {
                // Mettre les utilisateurs dans les ComboBox
                eleveComboBox.getItems().setAll(eleves);
                enseignantComboBox.getItems().setAll(enseignants);

                // Personnaliser l'affichage des ComboBox pour afficher le nom complet
                enseignantComboBox.setCellFactory(lv -> new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getPrenom() + " " + item.getNom());
                    }
                });

                eleveComboBox.setCellFactory(lv -> new ListCell<User>() {
                    @Override
                    protected void updateItem(User item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getPrenom() + " " + item.getNom());
                    }
                });

                // Rendre l'affichage correct pour le bouton du ComboBox (afficher le nom complet)
                enseignantComboBox.setButtonCell(enseignantComboBox.getCellFactory().call(null));
                eleveComboBox.setButtonCell(eleveComboBox.getCellFactory().call(null));

                // Si l'utilisateur est un enseignant connecté, sélectionner son nom dans le ComboBox
                if ("ENSEIGNANT".equalsIgnoreCase(user.getRole())) {
                    // Trouver l'objet User correspondant à l'enseignant
                    User enseignant = enseignants.stream()
                        .filter(u -> (u.getPrenom() + " " + u.getNom()).equals(user.getPrenom() + " " + user.getNom()))
                        .findFirst()
                        .orElse(null);

                    if (enseignant != null) {
                        enseignantComboBox.setValue(enseignant);
                        enseignantComboBox.setDisable(true);
                    }
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void fetchMatieres() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/matieres"))
            .header("Authorization", BEARER_TOKEN)
            .GET()
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(this::parseMatieres)
            .exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
    }

    private void parseMatieres(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<Matiere> matieres = Arrays.asList(mapper.readValue(responseBody, Matiere[].class));

            Platform.runLater(() -> {
                // Ajouter les objets Matiere directement au ComboBox
                matiereComboBox.getItems().setAll(matieres);

                // Afficher uniquement le libellé dans la liste déroulante
                matiereComboBox.setCellFactory(lv -> new ListCell<Matiere>() {
                    @Override
                    protected void updateItem(Matiere item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getLibelle());
                    }
                });

                // Rendu du bouton du ComboBox
                matiereComboBox.setButtonCell(matiereComboBox.getCellFactory().call(null));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void fetchNoteTypes() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/api/notes/type"))
            .header("Authorization", BEARER_TOKEN)
            .GET()
            .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(this::parseNoteTypes)
            .exceptionally(e -> {
                e.printStackTrace();
                return null;
            });
    }

    private void parseNoteTypes(String responseBody) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<NoteType> types = Arrays.asList(mapper.readValue(responseBody, NoteType[].class));

            Platform.runLater(() -> {
                // Ajouter les objets NoteType directement au ComboBox
                noteTypeComboBox.getItems().setAll(types);

                // Afficher uniquement le libellé dans la liste déroulante
                noteTypeComboBox.setCellFactory(lv -> new ListCell<NoteType>() {
                    @Override
                    protected void updateItem(NoteType item, boolean empty) {
                        super.updateItem(item, empty);
                        setText(empty || item == null ? null : item.getLibelle());
                    }
                });

                // Rendu du bouton du ComboBox
                noteTypeComboBox.setButtonCell(noteTypeComboBox.getCellFactory().call(null));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateNote() {
        Note selectedNote = notesTable.getSelectionModel().getSelectedItem();

        if (selectedNote == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une note à modifier.");
            return;
        }

        // Créer un dialogue de type alert personnalisé
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier une note");

        DialogPane dialogPane = new DialogPane();
        dialog.setDialogPane(dialogPane);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Réutilisation du formulaire (même contenu que celui d'ajout, mais dans un conteneur)
        VBox form = new VBox(10);
        form.getChildren().addAll(
                eleveComboBox,
                enseignantComboBox,
                matiereComboBox,
                valeurField,
                coefficientField,
                datePicker,
                noteTypeComboBox,
                commentaireField
        );
        dialogPane.setContent(form);

        // Préremplissage du formulaire avec les données de la note sélectionnée
        eleveComboBox.setValue(selectedNote.getEleve());
        enseignantComboBox.setValue(selectedNote.getEnseignant());
        matiereComboBox.setValue(selectedNote.getMatiere());
        valeurField.setText(String.valueOf(selectedNote.getValeur()));
        coefficientField.setText(String.valueOf(selectedNote.getCoefficient()));
        datePicker.setValue(LocalDate.parse(selectedNote.getDate()));
        String typeNom = selectedNote.getNoteType(); // Ex: "Contrôle"
        for (NoteType nt : noteTypeComboBox.getItems()) {
            if (nt.getLibelle().equalsIgnoreCase(typeNom)) {
                noteTypeComboBox.setValue(nt);
                break;
            }
        }

        commentaireField.setText(selectedNote.getCommentaire());

        // Attendre le retour utilisateur
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                updateNote(selectedNote.getId());
            }
        });
    }
    private void updateNote(int noteId) {
        try {
            User eleve = eleveComboBox.getValue();
            User enseignant = enseignantComboBox.getValue();
            Matiere matiere = matiereComboBox.getValue();
            NoteType noteType = noteTypeComboBox.getValue();

            double valeur = Double.parseDouble(valeurField.getText());
            double coefficient = Double.parseDouble(coefficientField.getText());
            String date = datePicker.getValue().toString();
            String commentaire = commentaireField.getText();

            String json = String.format(
                    "{"
                            + "\"eleve\": { \"id\": %d },"
                            + "\"enseignant\": { \"id\": %d },"
                            + "\"matiere\": { \"id\": %d },"
                            + "\"coefficient\": %s,"
                            + "\"valeur\": %s,"
                            + "\"noteType\": { \"id\": %d },"
                            + "\"commentaire\": \"%s\","
                            + "\"date\": \"%s\""
                            + "}",
                    eleve.getId(), enseignant.getId(), matiere.getId(),
                    coefficient, valeur, noteType.getId(),
                    commentaire, date
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + "/" + noteId))
                    .header("Authorization", BEARER_TOKEN)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> {
                        if (response.statusCode() == 200) {
                            Platform.runLater(() -> {
                                showAlert(AlertType.INFORMATION, "Note mise à jour avec succès.");
                                fetchNotes();
                                clearForm();
                            });
                        } else {
                            Platform.runLater(() -> showAlert(AlertType.ERROR, "Erreur lors de la mise à jour."));
                        }
                    })
                    .exceptionally(e -> {
                        e.printStackTrace();
                        Platform.runLater(() -> showAlert(AlertType.ERROR, "Erreur réseau : " + e.getMessage()));
                        return null;
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Veuillez vérifier les données saisies.");
        }
    }
}
