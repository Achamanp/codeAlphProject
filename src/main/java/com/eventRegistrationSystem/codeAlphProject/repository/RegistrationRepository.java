package com.eventRegistrationSystem.codeAlphProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventRegistrationSystem.codeAlphProject.entities.Registrations;
import com.eventRegistrationSystem.codeAlphProject.entities.User;
@Repository
public interface RegistrationRepository extends JpaRepository<Registrations , Long>{


	Registrations findByUser(User currentUser);

}
