package com.eventhub.bookingservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BookingRequest(

        @NotNull(message = "User id is required")
        @Positive(message = "User id must be greater than 0")
        Long userId,

        @NotNull(message = "Event id is required")
        @Positive(message = "Event id must be greater than 0")
        Long eventId

) {
}
