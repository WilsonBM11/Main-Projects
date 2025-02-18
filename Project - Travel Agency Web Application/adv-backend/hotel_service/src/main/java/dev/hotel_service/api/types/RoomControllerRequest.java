package dev.hotel_service.api.types;

import java.util.UUID;

public record RoomControllerRequest (
        Integer roomNumber,
        String details,
        Float price,
        UUID hotelId
){}