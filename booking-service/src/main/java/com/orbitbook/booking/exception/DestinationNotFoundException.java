package com.orbitbook.booking.exception;

public class DestinationNotFoundException
        extends RuntimeException {

    public DestinationNotFoundException(
            String message) {

        super(message);
    }
}