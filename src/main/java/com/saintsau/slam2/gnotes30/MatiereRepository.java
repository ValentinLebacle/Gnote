package com.saintsau.slam2.gnotes30;

import org.springframework.data.repository.CrudRepository;

public interface MatiereRepository extends CrudRepository<Matiere, Long> {
	Matiere findById(long id);
}
