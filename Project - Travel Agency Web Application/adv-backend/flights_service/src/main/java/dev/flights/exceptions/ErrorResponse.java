package dev.flights.exceptions;

public record ErrorResponse(
        String message,
        int code
) {
}