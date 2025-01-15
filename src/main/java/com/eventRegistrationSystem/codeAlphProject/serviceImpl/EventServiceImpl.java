package com.eventRegistrationSystem.codeAlphProject.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.eventRegistrationSystem.codeAlphProject.entities.Event;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventAlreadyCreatedException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.EventDto;
import com.eventRegistrationSystem.codeAlphProject.repository.EventRepository;
import com.eventRegistrationSystem.codeAlphProject.service.EventService;

import io.micrometer.common.util.StringUtils;

@Service
public class EventServiceImpl implements EventService{

	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Override
	public String createEvent(EventDto eventDto) throws EventAlreadyCreatedException {
	    if (eventDto == null) {
	        throw new IllegalArgumentException("EventDto cannot be null");
	    }
	    
	    // Check for required fields
	    if (StringUtils.isEmpty(eventDto.getTitle()) || StringUtils.isEmpty(eventDto.getDescription())) {
	        throw new IllegalArgumentException("Event title and description are required");
	    }
	    
	    // Check for duplicate event
	    Event existingEvent = this.eventRepository.findByTitleAndDescription(
	        eventDto.getTitle(),
	        eventDto.getDescription()
	    );
	    
	    if (existingEvent != null) {
	        throw new EventAlreadyCreatedException(
	            String.format("Event with title '%s' and description already exists", eventDto.getTitle())
	        );
	    }
	    
	    Event newEvent = this.modelMapper.map(eventDto, Event.class);
	   newEvent.setCreatedAt(LocalDateTime.now());
	   newEvent.setUpdatedAt(LocalDateTime.now());
	    
	    this.eventRepository.save(newEvent);
	    
	    // Return the ID or some identifier of the created event
	    return "Event Created Successfully !!"; // Assuming getId() returns String, adjust return type if needed
	}

	@Override
	public String updateEvent(EventDto eventDto) throws EventAlreadyCreatedException, EventNotFoundException {
	    // Validate input
	    if (eventDto == null) {
	        throw new IllegalArgumentException("EventDto cannot be null");
	    }
	    
	    if (eventDto.getId() == null) {
	        throw new IllegalArgumentException("Event ID is required for update");
	    }
	    
	    // Check if event exists
	    Event existingEvent = this.eventRepository.findById(eventDto.getId())
	        .orElseThrow(() -> new EventNotFoundException(
	            String.format("Event with ID '%s' not found", eventDto.getId())
	        ));
	    
	    // Check for duplicate title/description if they are being changed
	    if (!existingEvent.getTitle().equals(eventDto.getTitle()) || !existingEvent.getDescription().equals(eventDto.getDescription())) {
	        
	        Event duplicateEvent = this.eventRepository.findByTitleAndDescription(eventDto.getTitle(), eventDto.getDescription());
	        
	        if (duplicateEvent != null && !duplicateEvent.getId().equals(eventDto.getId())) {
	            throw new EventAlreadyCreatedException(
	                String.format("Event with title '%s' and description already exists", eventDto.getTitle())
	            );
	        }
	    }
	    
	    // Update the existing event
	    existingEvent.setTitle(eventDto.getTitle());
	    existingEvent.setDescription(eventDto.getDescription());
	   existingEvent.setUpdatedAt(LocalDateTime.now());
	   existingEvent.setTime(eventDto.getTime());
	   existingEvent.setVenue(eventDto.getVenue());
	   existingEvent.setDate(eventDto.getDate());
	   existingEvent.setAvailableSlot(eventDto.getAvailableSlot());
	    
	    // Save the updated event
	    this.eventRepository.save(existingEvent);
	    
	    return "Event Updated Successfully !!";
	}

	@Override
	public String deleteEvent(String title) throws EventNotFoundException {
	    if (StringUtils.isEmpty(title)) {
	        throw new IllegalArgumentException("Event title cannot be null or empty");
	    }

	    Event event = this.eventRepository.findByTitle(title);
	    if (event == null) {
	        throw new EventNotFoundException(
	            String.format("Event with title '%s' not found", title)
	        );
	    }

	    this.eventRepository.delete(event);
	    return "Event Deleted Successfully !!";
	}

	@Override
	public List<EventDto> getAllEvent(Integer pageNumber, Integer pageSize, String sortDir, String sortBy) {
	    // Set default values if parameters are null
	    pageNumber = (pageNumber == null) ? 0 : pageNumber;
	    pageSize = (pageSize == null) ? 10 : pageSize;
	    sortDir = (sortDir == null) ? "asc" : sortDir.toLowerCase();
	    sortBy = (sortBy == null) ? "title" : sortBy;

	    // Create Sort object based on direction
	    Sort sort = sortDir.equalsIgnoreCase("desc") ? 
	        Sort.by(sortBy).descending() : 
	        Sort.by(sortBy).ascending();

	    // Create Pageable object
	    Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

	    // Get page of events
	    Page<Event> eventPage = this.eventRepository.findAll(pageable);

	    // Convert to list of DTOs
	    List<EventDto> eventDtos = eventPage.getContent()
	        .stream()
	        .map(event -> this.modelMapper.map(event, EventDto.class))
	        .collect(Collectors.toList());

	    return eventDtos;
	}

	@Override
	public EventDto getEventByTitle(String title) throws EventNotFoundException {
	    if (StringUtils.isEmpty(title)) {
	        throw new IllegalArgumentException("Event title cannot be null or empty");
	    }

	    Event event = this.eventRepository.findByTitle(title);
	    if (event == null) {
	        throw new EventNotFoundException(
	            String.format("Event with title '%s' not found", title)
	        );
	    }

	    return this.modelMapper.map(event, EventDto.class);
	}

}
