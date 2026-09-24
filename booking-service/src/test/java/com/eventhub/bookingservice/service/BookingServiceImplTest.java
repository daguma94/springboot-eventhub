package com.eventhub.bookingservice.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.eventhub.bookingservice.client.EventClient;
import com.eventhub.bookingservice.client.UserClient;
import com.eventhub.bookingservice.repository.BookingRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.eventhub.bookingservice.dto.BookingRequest;
import com.eventhub.bookingservice.dto.BookingResponse;
import com.eventhub.bookingservice.entity.Booking;
import com.eventhub.bookingservice.entity.BookingStatus;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.doThrow;

import com.eventhub.bookingservice.exception.UserNotFoundException;

import feign.FeignException;

import com.eventhub.bookingservice.exception.EventNotFoundException;

import static org.mockito.Mockito.mock;

import com.eventhub.bookingservice.exception.ServiceUnavailableException;

import feign.RetryableException;

import java.util.Optional;

import com.eventhub.bookingservice.exception.BookingNotFoundException;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserClient userClient;

    @Mock
    private EventClient eventClient;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void createBookingShouldReturnCreatedBooking() {

        BookingRequest request = new BookingRequest(
            1L,
            1L
        );

        Booking savedBooking = new Booking(
            1L,
            1L,
            1L,
            BookingStatus.CONFIRMED
        );

        when(bookingRepository.save(any(Booking.class)))
            .thenReturn(savedBooking);

        BookingResponse response = bookingService.createBooking(request);

        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertEquals(1L, response.eventId());
        assertEquals(BookingStatus.CONFIRMED, response.status());

        verify(userClient).getUserById(1L);
        verify(eventClient).getEventById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBookingShouldThrowExceptionWhenUserDoesNotExist() {

        BookingRequest request = new BookingRequest(
            99L,
            1L
        );

        doThrow(FeignException.NotFound.class)
            .when(userClient)
            .getUserById(99L);

        UserNotFoundException exception = assertThrows(
            UserNotFoundException.class,
            () -> bookingService.createBooking(request)
        );

        assertEquals(
            "User not found with id: 99",
            exception.getMessage()
        );

        verify(userClient).getUserById(99L);
        verify(eventClient, never()).getEventById(any());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBookingShouldThrowExceptionWhenEventDoesNotExist() {

        BookingRequest request = new BookingRequest(
            1L,
            99L
        );

        doThrow(FeignException.NotFound.class)
            .when(eventClient)
            .getEventById(99L);

        EventNotFoundException exception = assertThrows(
            EventNotFoundException.class,
            () -> bookingService.createBooking(request)
        );

        assertEquals(
            "Event not found with id: 99",
            exception.getMessage()
        );

        verify(userClient).getUserById(1L);
        verify(eventClient).getEventById(99L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBookingShouldThrowExceptionWhenUserServiceIsUnavailable() {

        BookingRequest request = new BookingRequest(
            1L,
            1L
        );

        RetryableException retryableException =
            mock(RetryableException.class);

        doThrow(retryableException)
            .when(userClient)
            .getUserById(1L);

        ServiceUnavailableException exception = assertThrows(
            ServiceUnavailableException.class,
            () -> bookingService.createBooking(request)
        );

        assertEquals(
            "User service is currently unavailable",
            exception.getMessage()
        );

        verify(userClient).getUserById(1L);
        verify(eventClient, never()).getEventById(any());
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBookingShouldThrowExceptionWhenEventServiceIsUnavailable() {

        BookingRequest request = new BookingRequest(
            1L,
            1L
        );

        RetryableException retryableException =
            mock(RetryableException.class);

        doThrow(retryableException)
            .when(eventClient)
            .getEventById(1L);

        ServiceUnavailableException exception = assertThrows(
            ServiceUnavailableException.class,
            () -> bookingService.createBooking(request)
        );

        assertEquals(
            "Event service is currently unavailable",
            exception.getMessage()
        );

        verify(userClient).getUserById(1L);
        verify(eventClient).getEventById(1L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingByIdShouldReturnBookingWhenBookingExists() {

        Booking booking = new Booking(
            1L,
            1L,
            1L,
            BookingStatus.CONFIRMED
        );

        when(bookingRepository.findById(1L))
            .thenReturn(Optional.of(booking));

        BookingResponse response = bookingService.getBookingById(1L);

        assertEquals(1L, response.id());
        assertEquals(1L, response.userId());
        assertEquals(1L, response.eventId());
        assertEquals(BookingStatus.CONFIRMED, response.status());

        verify(bookingRepository).findById(1L);
    }

    @Test
    void getBookingByIdShouldThrowExceptionWhenBookingDoesNotExist() {

        when(bookingRepository.findById(99L))
            .thenReturn(Optional.empty());

        BookingNotFoundException exception = assertThrows(
            BookingNotFoundException.class,
            () -> bookingService.getBookingById(99L)
        );

        assertEquals(
            "Booking not found with id: 99",
            exception.getMessage()
        );

        verify(bookingRepository).findById(99L);
    }

    @Test
    void getAllBookingsShouldReturnAllBookings() {

        Booking booking1 = new Booking(
            1L,
            1L,
            1L,
            BookingStatus.CONFIRMED
        );

        Booking booking2 = new Booking(
            2L,
            2L,
            1L,
            BookingStatus.CONFIRMED
        );

        when(bookingRepository.findAll())
            .thenReturn(List.of(booking1, booking2));

        List<BookingResponse> responses = bookingService.getAllBookings();

        assertEquals(2, responses.size());

        assertEquals(1L, responses.get(0).id());
        assertEquals(1L, responses.get(0).userId());
        assertEquals(1L, responses.get(0).eventId());
        assertEquals(BookingStatus.CONFIRMED, responses.get(0).status());

        assertEquals(2L, responses.get(1).id());
        assertEquals(2L, responses.get(1).userId());
        assertEquals(1L, responses.get(1).eventId());
        assertEquals(BookingStatus.CONFIRMED, responses.get(1).status());

        verify(bookingRepository).findAll();
    }
}
