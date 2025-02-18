package dev.flights.api.types;

import dev.flights.handlers.types.FlightPriceAvailability;

import java.util.Optional;

public record ChangeDetailsFlightRequest(
        String flightId,
        Optional<FlightPriceAvailability> firstClass,
        Optional<FlightPriceAvailability> businessClass,
        Optional<FlightPriceAvailability> commercialClass
) {
}
