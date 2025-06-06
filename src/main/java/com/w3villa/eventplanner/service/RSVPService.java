package com.w3villa.eventplanner.service;

import com.w3villa.eventplanner.model.Event;
import com.w3villa.eventplanner.model.RSVP;
import com.w3villa.eventplanner.model.User;
import com.w3villa.eventplanner.repository.RSVPRepository;
import com.w3villa.eventplanner.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RSVPService {
    private static final Logger logger = LoggerFactory.getLogger(RSVPService.class);

    @Autowired
    private RSVPRepository rsvpRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserRepository userRepository;

    public RSVP submitRSVP(Long eventId, String status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        if (event.getStartTime().isBefore(LocalDateTime.now())) {
            logger.error("Cannot RSVP to past event ID: {}", eventId);
            throw new IllegalArgumentException("Cannot RSVP to past events");
        }
        try {
            RSVP.RSVPStatus rsvpStatus = RSVP.RSVPStatus.valueOf(status.toUpperCase());
            Optional<RSVP> existingRSVP = rsvpRepository.findByUserAndEvent(user, event);
            RSVP rsvp = existingRSVP.orElse(new RSVP());
            rsvp.setUser(user);
            rsvp.setEvent(event);
            rsvp.setStatus(rsvpStatus);
            logger.info("Submitting RSVP for user {} to event ID {} with status {}", username, eventId, status);
            return rsvpRepository.save(rsvp);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid RSVP status: {}", status);
            throw new IllegalArgumentException("Invalid RSVP status: " + status);
        }
    }

    public List<RSVP> getUserRSVPs() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
        List<RSVP> rsvps = rsvpRepository.findByUser(user);
        logger.info("Retrieved {} RSVPs for user {}", rsvps.size(), username);
        return rsvps;
    }

    public List<RSVP> getEventRSVPs(Long eventId) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + eventId));
        List<RSVP> rsvps = rsvpRepository.findByEvent(event);
        logger.info("Retrieved {} RSVPs for event ID {}", rsvps.size(), eventId);
        return rsvps;
    }
}