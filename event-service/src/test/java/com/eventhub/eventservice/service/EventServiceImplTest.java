package com.eventhub.eventservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventhub.eventservice.dto.EventRequest;
import com.eventhub.eventservice.dto.EventResponse;
import com.eventhub.eventservice.entity.Event;
import com.eventhub.eventservice.repository.EventRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import com.eventhub.eventservice.exception.EventNotFoundException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void createEventShouldReturnCreatedEvent() {

        LocalDateTime date = LocalDateTime.of(
            2027, 10, 15, 18, 0
        );

        EventRequest request = new EventRequest(
            "Spring Boot Conference",
            "Conference about Java and Spring Boot",
            "Madrid",
            date,
            100
        );

        Event savedEvent = new Event(
            1L,
            "Spring Boot Conference",
            "Conference about Java and Spring Boot",
            "Madrid",
            date,
            100
        );

        when(eventRepository.save(org.mockito.ArgumentMatchers.any(Event.class)))
            .thenReturn(savedEvent);

        EventResponse response = eventService.createEvent(request);

        assertEquals(1L, response.id());
        assertEquals("Spring Boot Conference", response.name());
        assertEquals("Madrid", response.location());
        assertEquals(date, response.date());
        assertEquals(100, response.capacity());

        verify(eventRepository)
            .save(org.mockito.ArgumentMatchers.any(Event.class));
    }

    @Test
    void getEventByIdShouldReturnEventWhenEventExists() {

        LocalDateTime date = LocalDateTime.of(
            2027, 10, 15, 18, 0
        );

        Event event = new Event(
            1L,
            "Spring Boot Conference",
            "Conference about Java and Spring Boot",
            "Madrid",
            date,
            100
        );

        when(eventRepository.findById(1L))
            .thenReturn(Optional.of(event));

        EventResponse response = eventService.getEventById(1L);

        assertEquals(1L, response.id());
        assertEquals("Spring Boot Conference", response.name());
        assertEquals("Madrid", response.location());
        assertEquals(date, response.date());
        assertEquals(100, response.capacity());

        verify(eventRepository).findById(1L);
    }

    @Test
    void getEventByIdShouldThrowExceptionWhenEventDoesNotExist() {

        when(eventRepository.findById(99L))
            .thenReturn(Optional.empty());

        EventNotFoundException exception = assertThrows(
            EventNotFoundException.class,
            () -> eventService.getEventById(99L)
        );

        assertEquals(
            "Event not found with id: 99",
            exception.getMessage()
        );

        verify(eventRepository).findById(99L);
    }

    @Test
    void getAllEventsShouldReturnAllEvents() {

        LocalDateTime date1 = LocalDateTime.of(
            2027, 10, 15, 18, 0
        );

        LocalDateTime date2 = LocalDateTime.of(
            2027, 11, 20, 20, 30
        );

        Event event1 = new Event(
            1L,
            "Spring Boot Conference",
            "Conference about Java and Spring Boot",
            "Madrid",
            date1,
            100
        );

        Event event2 = new Event(
            2L,
            "Java Meetup",
            "Meeting for Java developers",
            "Barcelona",
            date2,
            50
        );

        when(eventRepository.findAll())
            .thenReturn(List.of(event1, event2));

        List<EventResponse> responses = eventService.getAllEvents();

        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).id());
        assertEquals("Spring Boot Conference", responses.get(0).name());
        assertEquals("Madrid", responses.get(0).location());

        assertEquals(2L, responses.get(1).id());
        assertEquals("Java Meetup", responses.get(1).name());
        assertEquals("Barcelona", responses.get(1).location());

        verify(eventRepository).findAll();
    }
}
