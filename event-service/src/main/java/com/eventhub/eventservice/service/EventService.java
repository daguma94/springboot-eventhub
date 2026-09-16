package com.eventhub.eventservice.service;

import java.util.List;

import com.eventhub.eventservice.dto.EventRequest;
import com.eventhub.eventservice.dto.EventResponse;

public interface EventService {

    EventResponse createEvent(EventRequest request);

    List<EventResponse> getAllEvents();

    EventResponse getEventById(Long id);
}
