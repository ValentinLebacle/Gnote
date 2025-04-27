package com.saintsau.slam2.gnotes30.jpaRepository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.saintsau.slam2.gnotes30.entity.Matiere;

public interface MatiereRepository extends CrudRepository<Matiere, Integer> {
	Optional<Matiere> findById(Integer id);
}
