package com.eventRegistrationSystem.codeAlphProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventRegistrationSystem.codeAlphProject.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Role findByRoleName(String string);
}
