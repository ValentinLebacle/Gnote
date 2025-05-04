package com.saintsau.slam2.gnotes30.jpaRepositoryTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindById() {
        // Créer un utilisateur avec un mot de passe
        User user = new User();
        user.setNom("Guihéneuf");
        user.setPrenom("Léa");
        user.setEmail("lea@example.com");
        user.setPasswordHash("password123");  // Ajout du mot de passe

        // Sauvegarde
        User savedUser = userRepository.save(user);

        // Recherche
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assertions
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getNom()).isEqualTo("Guihéneuf");
        assertThat(foundUser.get().getEmail()).isEqualTo("lea@example.com");
    }

    @Test
    void testFindByEmail() {
        // Créer et sauvegarder un utilisateur avec un mot de passe
        User user = new User();
        user.setNom("Dupont");
        user.setPrenom("Paul");
        user.setEmail("paul@example.com");
        user.setPasswordHash("password456");  // Ajout du mot de passe
        userRepository.save(user);

        // Recherche par email
        Optional<User> found = userRepository.findByEmail("paul@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getPrenom()).isEqualTo("Paul");
    }

}
