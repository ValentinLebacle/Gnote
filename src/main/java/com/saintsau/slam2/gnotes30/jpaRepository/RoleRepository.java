package com.saintsau.slam2.gnotes30.jpaRepository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import com.saintsau.slam2.gnotes30.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findById(Integer id);
}