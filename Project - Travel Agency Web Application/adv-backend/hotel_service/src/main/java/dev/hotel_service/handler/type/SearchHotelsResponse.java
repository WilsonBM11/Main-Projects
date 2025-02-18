package dev.hotel_service.handler.type;

import java.util.List;
import java.util.UUID;

public record SearchHotelsResponse (
    UUID hotelId,
    String name,
    Integer phoneNumber,
    String email,
    String address,

    List<String> facilities
){}
