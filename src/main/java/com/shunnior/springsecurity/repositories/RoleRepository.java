package com.shunnior.springsecurity.repositories;

import com.shunnior.springsecurity.entities.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

}
