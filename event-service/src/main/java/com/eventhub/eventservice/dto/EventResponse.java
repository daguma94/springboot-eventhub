package com.eventhub.eventservice.dto;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String description,
        String location,
        LocalDateTime date,
        Integer capacity
) {
}
