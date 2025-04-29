package com.saintsau.slam2.gnotes30.jpaRepository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.saintsau.slam2.gnotes30.entity.Note;
import com.saintsau.slam2.gnotes30.entity.User;
import java.util.List;


public interface NoteRepository extends CrudRepository<Note, Long> {
    Optional<Note> findById(Long id);
    List<Note> findByEleveOrEnseignant(User eleve, User enseignant);
}
