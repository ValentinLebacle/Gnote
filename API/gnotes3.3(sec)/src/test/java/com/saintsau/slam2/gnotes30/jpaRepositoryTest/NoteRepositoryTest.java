package com.saintsau.slam2.gnotes30.jpaRepositoryTest;

import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.Role;
import com.saintsau.slam2.gnotes30.entity.User;
import com.saintsau.slam2.gnotes30.jpaRepository.NoteRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.RoleRepository;
import com.saintsau.slam2.gnotes30.jpaRepository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User enseignant;
    private User eleve;
    private Note note;
    
    @BeforeEach
    void setUp() {
        // Récupérer ou créer le rôle Enseignant
        Role roleEnseignant = roleRepository.findById(1)
            .orElseGet(() -> {
                Role r = new Role("Enseignant");
                return roleRepository.save(r);
            });

        // Récupérer ou créer le rôle Élève
        Role roleEleve = roleRepository.findById(2)
            .orElseGet(() -> {
                Role r = new Role("Élève");
                return roleRepository.save(r);
            });

        // Création et sauvegarde des utilisateurs avec les rôles correspondants
        enseignant = new User("Dupont", "Jean", roleEnseignant, "enseignant@test.com", "Adresse 1", "0101010101", "secret1");
        eleve = new User("Martin", "Alice", roleEleve, "eleve@test.com", "Adresse 2", "0202020202", "secret2");

        enseignant = userRepository.save(enseignant);
        eleve = userRepository.save(eleve);

        // Création et sauvegarde de la note
        note = new Note();
        note.setEnseignant(enseignant);
        note.setEleve(eleve);
        note.setValeur(new BigDecimal("17.50"));
        note = noteRepository.save(note);
    }




    @Test
    void testFindById() {
        Optional<Note> found = noteRepository.findById(note.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getValeur()).isEqualTo(new BigDecimal("17.50"));
        assertThat(found.get().getEleve().getId()).isEqualTo(eleve.getId());
        assertThat(found.get().getEnseignant().getId()).isEqualTo(enseignant.getId());
    }

    @Test
    void testFindByEleveOrEnseignant() {
        List<Note> notes = noteRepository.findByEleveOrEnseignant(eleve, enseignant);
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getValeur()).isEqualTo(new BigDecimal("17.50"));
    }
}
