package com.eventhub.eventservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventhub.eventservice.dto.EventRequest;
import com.eventhub.eventservice.dto.EventResponse;
import com.eventhub.eventservice.entity.Event;
import com.eventhub.eventservice.exception.EventNotFoundException;
import com.eventhub.eventservice.repository.EventRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Override
    public EventResponse createEvent(EventRequest request) {

        Event event = new Event();

        event.setName(request.name());
        event.setDescription(request.description());
        event.setLocation(request.location());
        event.setDate(request.date());
        event.setCapacity(request.capacity());

        Event savedEvent = eventRepository.save(event);

        return toResponse(savedEvent);
    }

    @Override
    public List<EventResponse> getAllEvents() {

        return eventRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EventResponse getEventById(Long id) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException(id));

        return toResponse(event);
    }

    private EventResponse toResponse(Event event) {

        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getDate(),
                event.getCapacity()
        );
    }
}
