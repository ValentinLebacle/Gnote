package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.Role;
import com.saintsau.slam2.gnotes30.entity.User;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertEquals(0, user.getId());
        assertNull(user.getNom());
        assertNull(user.getPrenom());
        assertNull(user.getRole());
        assertNull(user.getEmail());
        assertNull(user.getAdresse());
        assertNull(user.getTelephone());
        assertNull(user.getPasswordHash());
        assertNull(user.getNotesEnseignant());
        assertNull(user.getNotesEleve());
    }

    @Test
    void testParameterizedConstructor() {
        Role role = new Role("Enseignant");
        User user = new User("Doe", "John", role, "john.doe@example.com", "123 Main St", "123-456-7890", "hashedPassword123");
        
        assertEquals("Doe", user.getNom());
        assertEquals("John", user.getPrenom());
        assertEquals(role, user.getRole());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("123 Main St", user.getAdresse());
        assertEquals("123-456-7890", user.getTelephone());
        assertEquals("hashedPassword123", user.getPasswordHash());
    }

    @Test
    void testEmailConstructor() {
        User user = new User("john.doe@example.com", "hashedPassword123");
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getPasswordHash());
    }

    @Test
    void testSetAndGetId() {
        User user = new User();
        user.setId(1);
        assertEquals(1, user.getId());
    }

    @Test
    void testSetAndGetNom() {
        User user = new User();
        user.setNom("Doe");
        assertEquals("Doe", user.getNom());
    }

    @Test
    void testSetAndGetPrenom() {
        User user = new User();
        user.setPrenom("John");
        assertEquals("John", user.getPrenom());
    }

    @Test
    void testSetAndGetRole() {
        Role role = new Role("Admin");
        User user = new User();
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    void testSetAndGetEmail() {
        User user = new User();
        user.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void testSetAndGetAdresse() {
        User user = new User();
        user.setAdresse("123 Main St");
        assertEquals("123 Main St", user.getAdresse());
    }

    @Test
    void testSetAndGetTelephone() {
        User user = new User();
        user.setTelephone("123-456-7890");
        assertEquals("123-456-7890", user.getTelephone());
    }

    @Test
    void testSetAndGetPasswordHash() {
        User user = new User();
        user.setPasswordHash("hashedPassword123");
        assertEquals("hashedPassword123", user.getPasswordHash());
    }

    @Test
    void testSetAndGetNotesEnseignant() {
        User user = new User();
        Note note = new Note(); 
        user.setNotesEnseignant(Arrays.asList(note));
        assertEquals(1, user.getNotesEnseignant().size());
        assertEquals(note, user.getNotesEnseignant().get(0));
    }

    @Test
    void testSetAndGetNotesEleve() {
        User user = new User();
        Note note = new Note();
        user.setNotesEleve(Arrays.asList(note));
        assertEquals(1, user.getNotesEleve().size());
        assertEquals(note, user.getNotesEleve().get(0));
    }
}
