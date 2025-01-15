package com.eventRegistrationSystem.codeAlphProject.payloads;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EventDto {
	
	private Long id;
	 private String title;
	 private String description;
	 private String venue;
	 private LocalDateTime time;
	 private LocalDate date;
	 private Integer availableSlot;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public Integer getAvailableSlot() {
		return availableSlot;
	}
	public void setAvailableSlot(Integer availableSlot) {
		this.availableSlot = availableSlot;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	 
	 
}
