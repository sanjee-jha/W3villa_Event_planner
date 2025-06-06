package com.w3villa.eventplanner.controller;

import com.w3villa.eventplanner.model.Event;
import com.w3villa.eventplanner.model.RSVP;
import com.w3villa.eventplanner.service.EventService;
import com.w3villa.eventplanner.service.RSVPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private RSVPService rsvpService;

    @GetMapping("/admin/event/new")
    public String showCreateEventForm(Model model) {
        logger.info("Showing create event form");
        model.addAttribute("event", new Event());
        return "event-form";
    }

    @PostMapping("/admin/event")
    public String createEvent(@ModelAttribute Event event, Model model) {
        try {
            eventService.createEvent(event);
            logger.info("Event created successfully: {}", event.getTitle());
            model.addAttribute("message", "Event created successfully");
            return "redirect:/events";
        } catch (IllegalArgumentException e) {
            logger.error("Error creating event: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "event-form";
        }
    }

    @GetMapping("/admin/event/edit/{id}")
    public String showEditEventForm(@PathVariable Long id, Model model) {
        try {
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
            logger.info("Showing edit form for event ID: {}", id);
            model.addAttribute("event", event);
            return "event-form";
        } catch (IllegalArgumentException e) {
            logger.error("Error accessing event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/admin/event/edit/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event, Model model) {
        try {
            eventService.updateEvent(id, event);
            logger.info("Event updated successfully: ID {}", id);
            model.addAttribute("message", "Event updated successfully");
            return "redirect:/events";
        } catch (IllegalArgumentException e) {
            logger.error("Error updating event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "event-form";
        }
    }

    @PostMapping("/admin/event/delete/{id}")
    public String deleteEvent(@PathVariable Long id, Model model) {
        try {
            eventService.deleteEvent(id);
            logger.info("Event deleted successfully: ID {}", id);
            model.addAttribute("message", "Event deleted successfully");
            return "redirect:/events";
        } catch (IllegalArgumentException e) {
            logger.error("Error deleting event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping
    public String listEvents(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Current user: {}", auth.getName());
        logger.info("Authorities: {}", auth.getAuthorities());
        try {
            List<Event> events = eventService.getUpcomingEvents();
            model.addAttribute("events", events != null ? events : Collections.emptyList());
            logger.info("Listed {} upcoming events", events.size());
            return "event-list";
        } catch (Exception e) {
            logger.error("Error fetching events: {}", e.getMessage());
            model.addAttribute("error", "Error fetching events");
            return "error";
        }
    }

    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        try {
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
            logger.info("Viewing event details: ID {}", id);
            model.addAttribute("event", event);
            return "event-details";
        } catch (IllegalArgumentException e) {
            logger.error("Error accessing event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/event/{id}/rsvp")
    public String submitRSVP(@PathVariable Long id, @RequestParam String status, Model model) {
        try {
            rsvpService.submitRSVP(id, status);
            logger.info("RSVP submitted for event ID {} with status {}", id, status);
            model.addAttribute("message", "RSVP submitted successfully");
            return "redirect:/events/event/" + id;
        } catch (IllegalArgumentException e) {
            logger.error("Error submitting RSVP for event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "redirect:/events/event/" + id;
        }
    }

    @GetMapping("/my-rsvps")
    public String viewMyRSVPs(Model model) {
        try {
            List<RSVP> rsvps = rsvpService.getUserRSVPs();
            logger.info("Retrieved {} RSVPs for current user", rsvps.size());
            model.addAttribute("rsvps", rsvps);
            return "my-rsvps";
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching user RSVPs: {}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/admin/event/{id}/rsvps")
    public String viewEventRSVPs(@PathVariable Long id, Model model) {
        try {
            List<RSVP> rsvps = rsvpService.getEventRSVPs(id);
            long going = rsvps.stream().filter(r -> r.getStatus() == RSVP.RSVPStatus.GOING).count();
            long maybe = rsvps.stream().filter(r -> r.getStatus() == RSVP.RSVPStatus.MAYBE).count();
            long decline = rsvps.stream().filter(r -> r.getStatus() == RSVP.RSVPStatus.DECLINE).count();
            Event event = eventService.getEventById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Event not found with ID: " + id));
            logger.info("Viewing RSVP summary for event ID {}: Going={}, Maybe={}, Decline={}", id, going, maybe, decline);
            model.addAttribute("rsvps", rsvps);
            model.addAttribute("going", going);
            model.addAttribute("maybe", maybe);
            model.addAttribute("decline", decline);
            model.addAttribute("event", event);
            return "rsvp-summary";
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching RSVP summary for event ID {}: {}", id, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}