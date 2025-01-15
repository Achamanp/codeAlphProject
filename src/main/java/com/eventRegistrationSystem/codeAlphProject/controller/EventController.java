package com.eventRegistrationSystem.codeAlphProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventAlreadyCreatedException;
import com.eventRegistrationSystem.codeAlphProject.globalExceptions.EventNotFoundException;
import com.eventRegistrationSystem.codeAlphProject.payloads.ApiResponse;
import com.eventRegistrationSystem.codeAlphProject.payloads.EventDto;
import com.eventRegistrationSystem.codeAlphProject.service.EventService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createEvent(@Valid @RequestBody EventDto eventDto) {
        try {
            String result = this.eventService.createEvent(eventDto);
            return new ResponseEntity<>(
                new ApiResponse(result, true),
                HttpStatus.CREATED
            );
        } catch (EventAlreadyCreatedException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.CONFLICT
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateEvent(@Valid @RequestBody EventDto eventDto) {
        try {
            String result = this.eventService.updateEvent(eventDto);
            return new ResponseEntity<>(
                new ApiResponse(result, true),
                HttpStatus.OK
            );
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.NOT_FOUND
            );
        } catch (EventAlreadyCreatedException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.CONFLICT
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @DeleteMapping("/{title}")
    public ResponseEntity<ApiResponse> deleteEvent(@PathVariable String title) {
        try {
            String result = this.eventService.deleteEvent(title);
            return new ResponseEntity<>(
                new ApiResponse(result, true),
                HttpStatus.OK
            );
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.NOT_FOUND
            );
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                new ApiResponse(e.getMessage(), false),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortBy", required = false) String sortBy) {
        
        List<EventDto> events = this.eventService.getAllEvent(pageNumber, pageSize, sortDir, sortBy);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{title}")
    public ResponseEntity<EventDto> getEventByTitle(@PathVariable String title) {
        try {
            EventDto event = this.eventService.getEventByTitle(title);
            return new ResponseEntity<>(event, HttpStatus.OK);
        } catch (EventNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}