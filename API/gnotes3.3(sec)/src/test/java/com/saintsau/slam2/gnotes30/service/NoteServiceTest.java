package com.saintsau.slam2.gnotes30.service;

import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.NoteType;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
	
    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    private Note note;

    @BeforeEach
	void setUp() {
	    // Création d'un enseignant
	    User enseignant = new User();
	    enseignant.setId(1);
	    enseignant.setNom("Dupont");
	    enseignant.setPrenom("Michel");

	    // Création d'un élève
	    User eleve = new User();
	    eleve.setId(2);
	    eleve.setNom("Martin");
	    eleve.setPrenom("Jean");

	    // Création d'une matière
	    Matiere matiere = new Matiere();
	    matiere.setId(1);
	    matiere.setLibelle("Mathématiques");

	    // Création d'un type de note
	    NoteType noteType = new NoteType();
	    noteType.setId(1);
	    noteType.setLibelle("Contrôle");

	    // Création d'une note valide
	    note = new Note();
	    note.setId(1L);
	    note.setEnseignant(enseignant);
	    note.setEleve(eleve);
	    note.setMatiere(matiere);
	    note.setCoefficient(new BigDecimal("2"));
	    note.setValeur(new BigDecimal("15.5"));
	    note.setNoteType(noteType);
	    note.setCommentaire("Bon travail");
	    note.setDate(new Date());  // Date actuelle
	}

    @Test
    void shouldReturnAllNotes() {
        // Création d'un enseignant
        User enseignant = new User();
        enseignant.setId(1);
        enseignant.setNom("Dupont");
        enseignant.setPrenom("Michel");

        // Création d'un élève
        User eleve = new User();
        eleve.setId(2);
        eleve.setNom("Martin");
        eleve.setPrenom("Jean");

        // Création d'une matière
        Matiere matiere = new Matiere();
        matiere.setId(1);
        matiere.setLibelle("Mathématiques");

        // Création d'un type de note
        NoteType noteType = new NoteType();
        noteType.setId(1);
        noteType.setLibelle("Contrôle");

        // Création d'une note valide
        Note note = new Note();
        note.setId(1L);
        note.setEnseignant(enseignant);
        note.setEleve(eleve);
        note.setMatiere(matiere);
        note.setCoefficient(BigDecimal.valueOf(2));
        note.setValeur(BigDecimal.valueOf(15.5));
        note.setNoteType(noteType);
        note.setCommentaire("Bon travail");
        note.setDate(new Date());  // Date actuelle

        // Arrange
        when(noteRepository.findAll()).thenReturn(List.of(note));

        // Act
        List<Note> result = noteService.getAllNotes();

        // Assert
        assertEquals(1, result.size());
        // Comparer le libelle de la matière (en accédant directement à l'attribut)
        assertEquals("Mathématiques", result.get(0).getMatiere().getLibelle());
        verify(noteRepository, times(1)).findAll();
    }


    @Test
    void shouldReturnNoteById() {
        // Création d'un enseignant
        User enseignant = new User();
        enseignant.setId(1);
        enseignant.setNom("Dupont");
        enseignant.setPrenom("Michel");

        // Création d'une note pour l'enseignant
        Note note = new Note();
        note.setId(1L);
        note.setEnseignant(enseignant);
        note.setEleve(new User());  // Tu peux aussi créer un élève si nécessaire
        note.setMatiere(new Matiere());  // Créer une matière si nécessaire
        note.setNoteType(new NoteType());  // Créer un type de note si nécessaire
        note.setCoefficient(BigDecimal.valueOf(2));
        note.setValeur(BigDecimal.valueOf(15.5));
        note.setCommentaire("Bon travail");
        note.setDate(new Date());

        // Arrange
        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));

        // Act
        Optional<Note> result = noteService.getNoteById(1L);

        // Assert
        assertTrue(result.isPresent());
        // Comparer le nom et le prénom de l'enseignant
        assertEquals(enseignant.getPrenom() + " " + enseignant.getNom(), 
                     result.get().getEnseignant().getPrenom() + " " + result.get().getEnseignant().getNom());
        
        verify(noteRepository, times(1)).findById(1L);
    }


    @Test
    void shouldCreateNote() {
        // Arrange
        when(noteRepository.save(note)).thenReturn(note);

        // Act
        Note savedNote = noteService.createNote(note);

        // Assert
        assertNotNull(savedNote);
        // Convertir 15.5 en BigDecimal pour une comparaison correcte
        assertEquals(BigDecimal.valueOf(15.5), savedNote.getValeur());
        verify(noteRepository, times(1)).save(note);
    }


    @Test
    void shouldUpdateNote() {
    	
	    User enseignant = new User();
	    enseignant.setId(1);
	    enseignant.setNom("Dupont");
	    enseignant.setPrenom("Michel");

	    User eleve = new User();
	    eleve.setId(2);
	    eleve.setNom("Martin");
	    eleve.setPrenom("Jean");

	    Matiere matiere = new Matiere();
	    matiere.setId(1);
	    matiere.setLibelle("Physique");

	    NoteType noteType = new NoteType();
	    noteType.setId(1);
	    noteType.setLibelle("Examen");
    	
	    Date date = new Date();

        Note updatedNote = new Note();
        updatedNote.setEnseignant(enseignant);
        updatedNote.setEleve(eleve);
        updatedNote.setMatiere(matiere);
        updatedNote.setCoefficient(BigDecimal.valueOf(3));  // Modification ici
        updatedNote.setValeur(BigDecimal.valueOf(18.0));  // Utilisation de BigDecimal
        updatedNote.setNoteType(noteType);
        updatedNote.setCommentaire("Excellent");
        updatedNote.setDate(date);

        when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        when(noteRepository.save(any(Note.class))).thenReturn(updatedNote);

        // Act
        Note result = noteService.updateNote(1L, updatedNote);

        // Assert
        assertEquals(enseignant.getPrenom() + " " + enseignant.getNom(), result.getEnseignant().getPrenom() + " " + result.getEnseignant().getNom());  // Comparaison des noms
        assertEquals("Examen", result.getNoteType().getLibelle());
        assertEquals(BigDecimal.valueOf(18.0), result.getValeur());  // Comparaison des BigDecimal
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, times(1)).save(any(Note.class));
    }


    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentNote() {
        // Arrange
        when(noteRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> noteService.updateNote(1L, note)
        );

        assertEquals("Note non trouvée avec l'ID : 1", exception.getMessage());
        verify(noteRepository, times(1)).findById(1L);
        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void shouldDeleteNote() {
        // Act
        noteService.deleteNote(1L);

        // Assert
        verify(noteRepository, times(1)).deleteById(1L);
    }
}