package dev.hotel_service.exceptions;


import java.util.UUID;

public record ErrorResponse(
        String message,
        int code
) {

}