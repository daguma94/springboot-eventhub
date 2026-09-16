package com.eventhub.eventservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventhub.eventservice.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
