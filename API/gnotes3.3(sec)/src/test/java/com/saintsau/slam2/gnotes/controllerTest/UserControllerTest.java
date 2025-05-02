package com.saintsau.slam2.gnotes.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintsau.slam2.gnotes30.controller.UserController;
import com.saintsau.slam2.gnotes30.entity.Matiere;
import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.service.NoteService;
import com.saintsau.slam2.gnotes30.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllUsers() throws Exception {
        User user = new User();
        user.setId(1);
        user.setNom("Léa");

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nom").value("Léa"));
    }

    @Test
    void testGetUserById_Found() throws Exception {
        User user = new User();
        user.setId(1);
        user.setNom("Léa");

        Mockito.when(userService.getUserById(1)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nom").value("Léa"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        Mockito.when(userService.getUserById(99)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setNom("Léa");

        Mockito.when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nom").value("Léa"));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        User userDetails = new User();
        userDetails.setNom("NouvelleLéa");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setNom("NouvelleLéa");

        Mockito.when(userService.updateUser(eq(1), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nom").value("NouvelleLéa"));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        Mockito.when(userService.updateUser(eq(1), any(User.class))).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new User())))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGetNotesByUserId_Found() throws Exception {
        User user = new User();
        user.setId(1);

        Note note = new Note();
        note.setId(10L);

        Matiere matiere = new Matiere();
        matiere.setLibelle("Maths");
        note.setMatiere(matiere);

        Mockito.when(userService.getUserById(1)).thenReturn(Optional.of(user));
        Mockito.when(noteService.getNotesByUser(user)).thenReturn(List.of(note));

        mockMvc.perform(get("/api/users/1/notes"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(10))
            .andExpect(jsonPath("$[0].matiere.libelle").value("Maths"));
    }

    @Test
    void testGetNotesByUserId_NotFound() throws Exception {
        Mockito.when(userService.getUserById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1/notes"))
            .andExpect(status().isNotFound());
    }

    @Configuration
    public static class WebConfig {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
