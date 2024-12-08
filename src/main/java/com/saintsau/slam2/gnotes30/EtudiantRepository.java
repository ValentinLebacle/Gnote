package com.saintsau.slam2.gnotes30;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface EtudiantRepository extends CrudRepository<Etudiant, Long> {
	Optional<Etudiant>  findById(Long id);
}
