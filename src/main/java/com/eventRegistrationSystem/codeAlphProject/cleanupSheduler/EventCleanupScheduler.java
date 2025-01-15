package com.eventRegistrationSystem.codeAlphProject.cleanupSheduler;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eventRegistrationSystem.codeAlphProject.entities.Event;
import com.eventRegistrationSystem.codeAlphProject.repository.EventRepository;

import jakarta.annotation.PostConstruct;

@Component
public class EventCleanupScheduler {
	Logger log = LoggerFactory.getLogger(EventCleanupScheduler.class);

    @Autowired
    private EventRepository eventRepository;

    // Run at 1:00 AM every day
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanupPastEvents() {
        log.info("Starting cleanup of past events...");
        
        try {
            // Get current date at start of day
            LocalDate currentDate = LocalDate.now();
            
            // Find and delete all events with dates before current date
            List<Event> pastEvents = eventRepository.findByDateBefore(currentDate);
            
            if (!pastEvents.isEmpty()) {
                log.info("Found {} past events to delete", pastEvents.size());
                
                // Log events being deleted (optional)
                pastEvents.forEach(event -> 
                    log.debug("Deleting past event: {} scheduled for {}", 
                        event.getTitle(), 
                        event.getDate())
                );
                
                eventRepository.deleteAll(pastEvents);
                log.info("Successfully deleted {} past events", pastEvents.size());
            } else {
                log.info("No past events found to delete");
            }
            
        } catch (Exception e) {
            log.error("Error during past events cleanup: {}", e.getMessage(), e);
        }
    }
    
    // Optional: Run cleanup on application startup
    @PostConstruct
    public void cleanupOnStartup() {
        log.info("Performing initial cleanup on application startup...");
        cleanupPastEvents();
    }
}