package com.eventhub.bookingservice.dto;

import com.eventhub.bookingservice.entity.BookingStatus;

public record BookingResponse(
        Long id,
        Long userId,
        Long eventId,
        BookingStatus status
) {
}
