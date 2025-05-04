package com.saintsau.slam2.gnotes30.entityTest;

import org.junit.jupiter.api.Test;

import com.saintsau.slam2.gnotes30.entity.Role;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testDefaultConstructor() {
        Role role = new Role();
        assertEquals(0, role.getId());
        assertNull(role.getLibelle());
    }

    @Test
    void testParameterizedConstructor() {
        Role role = new Role("Administrateur");
        assertEquals("Administrateur", role.getLibelle());
    }

    @Test
    void testSetAndGetId() {
        Role role = new Role();
        role.setId(10);
        assertEquals(10, role.getId());
    }

    @Test
    void testSetAndGetLibelle() {
        Role role = new Role();
        role.setLibelle("Utilisateur");
        assertEquals("Utilisateur", role.getLibelle());
    }
}
