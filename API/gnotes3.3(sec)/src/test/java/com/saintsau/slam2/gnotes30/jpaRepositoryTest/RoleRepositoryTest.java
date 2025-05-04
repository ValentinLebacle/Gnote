package com.saintsau.slam2.gnotes30.jpaRepositoryTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import com.saintsau.slam2.gnotes30.entity.Role;
import com.saintsau.slam2.gnotes30.jpaRepository.RoleRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindById_AdminExists() {
        Optional<Role> foundRole = roleRepository.findById(1);
        assertTrue(foundRole.isPresent(), "Le rôle avec l'ID 1 devrait exister");
        assertEquals("ADMIN", foundRole.get().getLibelle(), "Le libellé devrait être 'ADMIN'");
    }

    @Test
    public void testFindById_EnseignantExists() {
        Optional<Role> foundRole = roleRepository.findById(2);
        assertTrue(foundRole.isPresent(), "Le rôle avec l'ID 2 devrait exister");
        assertEquals("Enseignant", foundRole.get().getLibelle(), "Le libellé devrait être 'ENSEIGNANT'");
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<Role> foundRole = roleRepository.findById(999);
        assertFalse(foundRole.isPresent(), "Aucun rôle ne devrait exister avec l'ID 999");
    }

    @Test
    public void testCountRoles() {
        long count = roleRepository.count();
        assertEquals(3, count, "Il devrait y avoir exactement 3 rôles dans la base");
    }
}
