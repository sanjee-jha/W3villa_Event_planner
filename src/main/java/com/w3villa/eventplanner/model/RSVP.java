package com.w3villa.eventplanner.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "rsvp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RSVP {  @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RSVPStatus status;

    public enum RSVPStatus {
        GOING, MAYBE, DECLINE
    }}