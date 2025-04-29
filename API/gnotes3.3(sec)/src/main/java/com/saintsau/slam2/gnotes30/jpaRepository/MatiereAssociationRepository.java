package com.saintsau.slam2.gnotes30.jpaRepository;

import com.saintsau.slam2.gnotes30.entity.MatiereAssociation;
import com.saintsau.slam2.gnotes30.entity.MatiereAssociationId;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MatiereAssociationRepository extends CrudRepository<MatiereAssociation, MatiereAssociationId> {
	 List<MatiereAssociation> findByUserId(Integer userId);
}
