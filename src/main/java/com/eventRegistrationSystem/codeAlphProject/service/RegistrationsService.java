package com.eventRegistrationSystem.codeAlphProject.service;
import java.util.List;
import com.eventRegistrationSystem.codeAlphProject.entities.Registrations;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.BusinessException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.ConcurrencyException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.RegistrationNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.TicketNotFoundException;

public interface RegistrationsService {
	public Registrations bookEvent(String eventTitle) throws ConcurrencyException, EventNotFoundException, BusinessException;
	public String cancleRegistration(String username) throws RegistrationNotFoundException, BusinessException, ConcurrencyException;
	public List<Registrations> getAllTickets(Integer pageNumber, Integer pageSize, String sortDir, String sortBy);
	Registrations getTicket() throws TicketNotFoundException;
	
}
