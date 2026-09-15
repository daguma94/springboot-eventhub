package com.eventhub.userservice.dto;

public record UserResponse(
        Long id,
        String name,
        String email
) {
}
