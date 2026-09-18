package com.eventhub.bookingservice.exception;

public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String serviceName) {
        super(serviceName + " is currently unavailable");
    }
}
