package com.w3villa.eventplanner.repository;

import com.w3villa.eventplanner.model.Event;
import com.w3villa.eventplanner.model.RSVP;
import com.w3villa.eventplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RSVPRepository extends JpaRepository<RSVP, Long> {
    Optional<RSVP> findByUserAndEvent(User user, Event event);
    List<RSVP> findByUser(User user);
    List<RSVP> findByEvent(Event event);
}