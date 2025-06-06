package com.w3villa.eventplanner.service;

import com.w3villa.eventplanner.model.Event;
import com.w3villa.eventplanner.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(Event event) {
        if (event.getStartTime().isAfter(event.getEndTime())) {
            logger.error("Invalid event times: startTime={} is after endTime={}", event.getStartTime(), event.getEndTime());
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (event.getTitle() == null || event.getTitle().trim().isEmpty() ||
            event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            logger.error("Invalid event: title or location is empty");
            throw new IllegalArgumentException("Title and location are required");
        }
        logger.info("Creating event: {}", event.getTitle());
        return eventRepository.save(event);
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
        if (updatedEvent.getStartTime().isAfter(updatedEvent.getEndTime())) {
            logger.error("Invalid event times: startTime={} is after endTime={}", updatedEvent.getStartTime(), updatedEvent.getEndTime());
            throw new IllegalArgumentException("Start time must be before end time");
        }
        if (updatedEvent.getTitle() == null || updatedEvent.getTitle().trim().isEmpty() ||
            updatedEvent.getLocation() == null || updatedEvent.getLocation().trim().isEmpty()) {
            logger.error("Invalid event: title or location is empty");
            throw new IllegalArgumentException("Title and location are required");
        }
        event.setTitle(updatedEvent.getTitle());
        event.setDescription(updatedEvent.getDescription());
        event.setStartTime(updatedEvent.getStartTime());
        event.setEndTime(updatedEvent.getEndTime());
        event.setLocation(updatedEvent.getLocation());
        logger.info("Updating event with ID: {}", id);
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            logger.error("Cannot delete event with ID {}: not found", id);
            throw new IllegalArgumentException("Event not found with ID: " + id);
        }
        logger.info("Deleting event with ID: {}", id);
        eventRepository.deleteById(id);
    }

    public List<Event> getUpcomingEvents() {
        try {
            List<Event> events = eventRepository.findByStartTimeGreaterThanEqualOrderByStartTimeAsc(LocalDateTime.now());
            logger.info("Retrieved {} upcoming events", events.size());
            return events;
        } catch (Exception e) {
            logger.error("Error fetching events: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public Optional<Event> getEventById(Long id) {
        Optional<Event> event = eventRepository.findById(id);
        logger.info("Event with ID {}: {}", id, event.isPresent() ? event.get().getTitle() : "Not found");
        return event;
    }
}