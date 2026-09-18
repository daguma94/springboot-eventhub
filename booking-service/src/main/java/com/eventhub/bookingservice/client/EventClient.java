package com.eventhub.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "event-service",
        url = "${event-service.url}"
)
public interface EventClient {

    @GetMapping("/events/{id}")
    void getEventById(@PathVariable Long id);
}
