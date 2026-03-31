package com.bookmyshow.exceptions;

public class SeatTemporarilyUnavailableException extends RuntimeException {
    public SeatTemporarilyUnavailableException(String message) {
        super(message);
    }
}
