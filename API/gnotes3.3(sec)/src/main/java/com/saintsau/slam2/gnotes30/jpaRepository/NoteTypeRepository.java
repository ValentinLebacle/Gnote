package com.saintsau.slam2.gnotes30.jpaRepository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.saintsau.slam2.gnotes30.entity.NoteType;

public interface NoteTypeRepository extends CrudRepository<NoteType, Integer> {
    Optional<NoteType> findById(Integer id);
}
