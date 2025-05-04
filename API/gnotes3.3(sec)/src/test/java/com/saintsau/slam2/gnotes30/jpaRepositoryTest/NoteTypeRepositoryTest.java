package com.saintsau.slam2.gnotes30.jpaRepositoryTest;

import com.saintsau.slam2.gnotes30.entity.NoteType;
import com.saintsau.slam2.gnotes30.jpaRepository.NoteTypeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NoteTypeRepositoryTest {

    @Autowired
    private NoteTypeRepository noteTypeRepository;

    private NoteType noteType;

    @BeforeEach
    void setUp() {
        // Essayer de récupérer un type existant ou en créer un avec un ID explicite
        noteType = noteTypeRepository.findById(1)
            .orElseGet(() -> {
                NoteType nt = new NoteType();
                nt.setId(1); // ID explicite pour éviter l'erreur MySQL
                nt.setLibelle("Contrôle continu");
                return noteTypeRepository.save(nt);
            });
    }


    @Test
    public void testFindAllNoteTypes() {
        // Enregistrer un objet pour effectuer le test
        noteTypeRepository.save(noteType);

        // Récupérer toutes les entités et vérifier qu'il y en a au moins une
        assertThat(noteTypeRepository.findAll()).isNotEmpty();
    }
}





