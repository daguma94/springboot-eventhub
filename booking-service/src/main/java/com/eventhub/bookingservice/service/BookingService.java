package com.eventhub.bookingservice.service;

import java.util.List;

import com.eventhub.bookingservice.dto.BookingRequest;
import com.eventhub.bookingservice.dto.BookingResponse;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request);

    List<BookingResponse> getAllBookings();

    BookingResponse getBookingById(Long id);
}
