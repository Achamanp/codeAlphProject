package com.eventRegistrationSystem.codeAlphProject.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eventRegistrationSystem.codeAlphProject.entities.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

	Event findByTitleAndDescription(String title, String description);

	Event findByTitle(String title);
	List<Event> findByDateBefore(LocalDate date);
	
}
