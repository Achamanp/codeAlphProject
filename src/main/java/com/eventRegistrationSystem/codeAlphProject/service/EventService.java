package com.eventRegistrationSystem.codeAlphProject.service;

import java.util.List;

import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventAlreadyCreatedException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.EventDto;

public interface EventService {
	
	public String createEvent(EventDto eventDto) throws EventAlreadyCreatedException;
	public String updateEvent(EventDto eventDto) throws EventAlreadyCreatedException, EventNotFoundException;
	public List<EventDto> getAllEvent(Integer pageNumber , Integer pageSize, String sortDir,String sortBy);
	public EventDto getEventByTitle(String title) throws EventNotFoundException;
	String deleteEvent(String title) throws EventNotFoundException;
	

}
