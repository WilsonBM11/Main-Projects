package dev.flights.handlers.types;

import java.util.List;

public record FlightReservationResponse(
        String id,
        String airline,
        String departure,
        String destination,
        String departureTime,
        String arrivalTime,
        List<FlightSeatResponse> seats
) {
}

