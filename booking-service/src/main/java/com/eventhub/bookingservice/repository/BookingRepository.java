package com.eventhub.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eventhub.bookingservice.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
