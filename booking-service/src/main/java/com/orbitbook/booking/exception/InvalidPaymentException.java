package com.orbitbook.booking.exception;

public class InvalidPaymentException
        extends RuntimeException {

    public InvalidPaymentException(
            String message) {

        super(message);
    }
}