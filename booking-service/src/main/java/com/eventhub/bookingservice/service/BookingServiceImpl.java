package com.eventhub.bookingservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventhub.bookingservice.dto.BookingRequest;
import com.eventhub.bookingservice.dto.BookingResponse;
import com.eventhub.bookingservice.entity.Booking;
import com.eventhub.bookingservice.entity.BookingStatus;
import com.eventhub.bookingservice.exception.BookingNotFoundException;
import com.eventhub.bookingservice.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public BookingResponse createBooking(BookingRequest request) {

        Booking booking = new Booking();

        booking.setUserId(request.userId());
        booking.setEventId(request.eventId());
        booking.setStatus(BookingStatus.CONFIRMED);

        Booking savedBooking = bookingRepository.save(booking);

        return toResponse(savedBooking);
    }

    @Override
    public List<BookingResponse> getAllBookings() {

        return bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public BookingResponse getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));

        return toResponse(booking);
    }

    private BookingResponse toResponse(Booking booking) {

        return new BookingResponse(
                booking.getId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getStatus()
        );
    }
}
