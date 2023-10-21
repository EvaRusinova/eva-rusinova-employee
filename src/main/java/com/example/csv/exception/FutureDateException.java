package com.example.csv.exception;

public class FutureDateException extends RuntimeException {

    public FutureDateException(String message) {
        super(message);
    }
}
