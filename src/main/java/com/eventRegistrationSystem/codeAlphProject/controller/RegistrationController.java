package com.eventRegistrationSystem.codeAlphProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventRegistrationSystem.codeAlphProject.entities.Registrations;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.BusinessException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.ConcurrencyException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.RegistrationNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.TicketNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.ApiResponse;
import com.eventRegistrationSystem.codeAlphProject.service.RegistrationsService;

@RestController
@RequestMapping("/api/registrations")
public class RegistrationController {
    
    @Autowired
    private RegistrationsService registrationsService;
    
    @PostMapping("/book/{eventTitle}")
    public ResponseEntity<?> bookEvent(@PathVariable String eventTitle) {
        try {
            Registrations registration = registrationsService.bookEvent(eventTitle);
            return ResponseEntity.ok(registration);
        } catch (EventNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (ConcurrencyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }
    
    @PostMapping("/cancel/{username}")
    public ResponseEntity<?> cancelRegistration(@PathVariable String username) {
        try {
            registrationsService.cancleRegistration(username);
            return ResponseEntity.ok(new ApiResponse("Registration cancelled successfully", true));
        } catch (RegistrationNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse(e.getMessage(), false));
        } catch (ConcurrencyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }
    
    @GetMapping("/ticket")
    public ResponseEntity<?> getTicket() {
        try {
            Registrations ticket = registrationsService.getTicket();
            return ResponseEntity.ok(ticket);
        } catch (TicketNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(e.getMessage(), false));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllTickets(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "registrationDate") String sortBy) {
        
        List<Registrations> tickets = registrationsService.getAllTickets(
            pageNumber, pageSize, sortDir, sortBy);
        return ResponseEntity.ok(tickets);
    }
}