package com.eventRegistrationSystem.codeAlphProject.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="event_table")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="event_id")
	private Long id;
	@Column(name="event_title" )
	@NotBlank(message = "Title can not be null")
	@Size(min = 50, max = 100 , message = "min 50 and max 100 words are allowed !!")
	private String title;
	@Column(name="event_description")
	@NotBlank(message = "Description cannot be blank.")
    @Size(min = 100, max = 350, message = "Description must be between 100 and 350 characters long.")
    private String description;
	@Column(name="event_date")
    @NotNull(message = "Date cannot be null.")
    @Future(message = "Date must be in the future.")
    private LocalDate date;
	@Column(name = "event_time")
    @NotNull(message = "Time cannot be null.")
    @Future(message = "Time must be in the future.")
    private LocalDateTime time;

    @NotBlank(message = "Venue cannot be blank.")
    @Size(min = 5, max = 100, message = "Venue must be between 5 and 100 characters long.")
    private String venue;

    @NotNull(message = "Available slot cannot be null.")
    @Min(value = 1, message = "Available slot must be at least 1.")
    private Integer availableSlot;

    @NotNull(message = "Creation time cannot be null.")
    @PastOrPresent(message = "Creation time must be in the past or present.")
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
    
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registrations> registrations = new ArrayList<>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public LocalDateTime getTime() {
		return time;
	}
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public Integer getAvailableSlot() {
		return availableSlot;
	}
	public void setAvailableSlot(Integer availableSlot) {
		this.availableSlot = availableSlot;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	} 
}
