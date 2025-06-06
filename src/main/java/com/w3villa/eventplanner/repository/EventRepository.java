package com.w3villa.eventplanner.repository;

import com.w3villa.eventplanner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByStartTimeGreaterThanEqualOrderByStartTimeAsc(LocalDateTime startTime);
}