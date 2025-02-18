package dev.hotel_service.api.types;

import java.util.List;

public record HotelControllerRequest (
        String name,
        Integer phoneNumber,
        String email,
        String address,
        List<String> facilities
) {}
