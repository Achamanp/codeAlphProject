package com.eventRegistrationSystem.codeAlphProject.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eventRegistrationSystem.codeAlphProject.entities.Event;
import com.eventRegistrationSystem.codeAlphProject.entities.Registrations;
import com.eventRegistrationSystem.codeAlphProject.entities.User;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.BusinessException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.ConcurrencyException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.RegistrationNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.TicketNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.repository.EventRepository;
import com.eventRegistrationSystem.codeAlphProject.repository.RegistrationRepository;
import com.eventRegistrationSystem.codeAlphProject.repository.UserRepository;
import com.eventRegistrationSystem.codeAlphProject.service.RegistrationsService;

import jakarta.transaction.Transactional;


	@Service
	public class RegistrationsServiceImpl implements RegistrationsService {
	    
	    @Autowired
	    private RegistrationRepository registrationsRepository;
	    
	    @Autowired
	    private EventRepository eventRepository;
	    
	    @Autowired
	    private UserRepository userRepository;

	    @Override
	    @Transactional
	    public Registrations bookEvent(String eventTitle) throws ConcurrencyException, EventNotFoundException, BusinessException {
	        try {
	            Event event = eventRepository.findByTitle(eventTitle);
	            if(event==null) {
	                throw new EventNotFoundException("Event not found with title: " + eventTitle);
	            }

	            if (event.getAvailableSlot() <= 0) {
	                throw new BusinessException("No seats available for this event");
	            }

	            Registrations registration = new Registrations();
	            registration.setEvent(event);
	            registration.setUser(getCurrentUser());
	            registration.setRegistrationDate(LocalDateTime.now());
	            registration.setStatus("CONFIRMED");

	            event.setAvailableSlot(event.getAvailableSlot() - 1);
	            eventRepository.save(event);

	            return registrationsRepository.save(registration);

	        } catch (ObjectOptimisticLockingFailureException e) {
	            throw new ConcurrencyException("Another user has modified this event. Please try again.");
	        }
	    }

	    @Override
	    @Transactional
	    public String cancleRegistration(String username) throws RegistrationNotFoundException, BusinessException, ConcurrencyException {
	        try {
	            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
	            if (!currentUsername.equals(username)) {
	                throw new SecurityException("You don't have permission to cancel this registration");
	            }

	            Registrations registration = registrationsRepository.findByUser(getCurrentUser());
	            if(registration==null) {
	                throw new RegistrationNotFoundException("Registration not found for user: " + username);
	            }

	            if (!isCancellationAllowed(registration)) {
	                throw new BusinessException("Cancellation not allowed for this registration");
	            }

	            Event event = registration.getEvent();
	            event.setAvailableSlot(event.getAvailableSlot() + 1);
	            eventRepository.save(event);

	            registration.setStatus("CANCELLED");
	            registrationsRepository.save(registration);

	            return "Registration cancelled successfully";

	        } catch (ObjectOptimisticLockingFailureException e) {
	            throw new ConcurrencyException("Concurrent modification detected. Please try again.");
	        }
	    }

	    @Override
	    public Registrations getTicket() throws TicketNotFoundException {
	        
	        Registrations registrations =  registrationsRepository.findByUser(getCurrentUser());
	        if(registrations==null) {
	            throw new TicketNotFoundException("No ticket found for current user");
	        }
	        return registrations;
	    }

	    @Override
	    public List<Registrations> getAllTickets(Integer pageNumber, Integer pageSize, String sortDir, String sortBy) {
	        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? 
	            Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
	        
	        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
	        Page<Registrations> pageRegistrations = registrationsRepository.findAll(pageable);
	        
	        return pageRegistrations.getContent();
	    }

	    private boolean isCancellationAllowed(Registrations registration) {
	        LocalDate eventDate = registration.getEvent().getDate();
	        LocalDate cancellationDeadline = eventDate.minusDays(1);
	        return LocalDate.now().isBefore(cancellationDeadline);
	    }

	    private User getCurrentUser() {
	        String username = SecurityContextHolder.getContext().getAuthentication().getName();
	        return userRepository.findByEmail(username)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	    }
	}
