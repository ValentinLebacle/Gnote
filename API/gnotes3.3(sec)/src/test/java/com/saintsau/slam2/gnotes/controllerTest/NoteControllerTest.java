package com.saintsau.slam2.gnotes.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintsau.slam2.gnotes30.controller.NoteController;
import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.NoteType;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.NoteService;
import com.saintsau.slam2.gnotes30.service.NoteTypeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.hamcrest.Matchers.containsString;


@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService noteService;

    @Mock
    private NoteTypeService noteTypeService;

    @InjectMocks
    private NoteController noteController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Note note1;
    private Note note2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(noteController).build();
        objectMapper = new ObjectMapper();

        NoteType noteType1 = new NoteType("Exam");
        NoteType noteType2 = new NoteType("Quiz");
        Matiere matiere = new Matiere();
        User user = new User();

        note1 = new Note(user, user, matiere, new BigDecimal(1), new BigDecimal(15), noteType1, "Maths", new Date());
        note1.setId(1L);
        note2 = new Note(user, user, matiere, new BigDecimal(1), new BigDecimal(17), noteType2, "Anglais", new Date());
        note2.setId(2L);
    }

    @Test
    void testGetAllNotes() throws Exception {
        when(noteService.getAllNotes()).thenReturn(List.of(note1, note2));

        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                // Vérification de la liste des notes
                .andExpect(jsonPath("$._embedded.noteList[0].nom").value("Maths"))
                .andExpect(jsonPath("$._embedded.noteList[1].nom").value("Anglais"))
                // Vérification des liens HATEOAS pour chaque note
                .andExpect(jsonPath("$._embedded.noteList[0]._links.self.href", containsString("/api/notes/1")))
                .andExpect(jsonPath("$._embedded.noteList[1]._links.self.href", containsString("/api/notes/2")))
                // Vérification du lien général
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes")));
    }

    @Test
    void testGetNoteById() throws Exception {
        when(noteService.getNoteById(1L)).thenReturn(Optional.of(note1));

        mockMvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Maths"))
                // Vérification du lien HATEOAS pour la note
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes/1")));

        when(noteService.getNoteById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNote() throws Exception {
        when(noteService.createNote(any(Note.class))).thenReturn(note1);

        mockMvc.perform(post("/api/notes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Maths"))
                // Vérification du lien HATEOAS pour la note créée
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes/1")));
    }

    @Test
    void testUpdateNote() throws Exception {
        when(noteService.updateNote(eq(1L), any(Note.class))).thenReturn(note2);

        mockMvc.perform(put("/api/notes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Anglais"))
                // Vérification du lien HATEOAS pour la note mise à jour
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes/1")));

        when(noteService.updateNote(eq(999L), any(Note.class))).thenThrow(new RuntimeException());

        mockMvc.perform(put("/api/notes/999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(note2)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNote() throws Exception {
        doNothing().when(noteService).deleteNote(1L);

        mockMvc.perform(delete("/api/notes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllNoteTypes() throws Exception {
        NoteType noteType1 = new NoteType("Exam");
        NoteType noteType2 = new NoteType("Quiz");

        noteType1.setId(1);
        noteType2.setId(2);

        when(noteTypeService.getAllNoteTypes()).thenReturn(List.of(noteType1, noteType2));

        mockMvc.perform(get("/api/notes/type"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.noteTypeList[0].nom").value("Exam"))
                .andExpect(jsonPath("$._embedded.noteTypeList[1].nom").value("Quiz"))
                // Vérification des liens HATEOAS pour les types de note
                .andExpect(jsonPath("$._embedded.noteTypeList[0]._links.self.href", containsString("/api/notes/type/1")))
                .andExpect(jsonPath("$._embedded.noteTypeList[1]._links.self.href", containsString("/api/notes/type/2")))
                // Vérification du lien général
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes/type")));
    }

    @Test
    void testGetNoteTypeById() throws Exception {
        NoteType noteType = new NoteType("Exam");
        noteType.setId(1);

        when(noteTypeService.getNoteTypeById(1)).thenReturn(Optional.of(noteType));

        mockMvc.perform(get("/api/notes/type/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Exam"))
                // Vérification du lien HATEOAS pour le type de note
                .andExpect(jsonPath("$._links.self.href", containsString("/api/notes/type/1")));

        when(noteTypeService.getNoteTypeById(999)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/notes/type/999"))
                .andExpect(status().isNotFound());
    }
}