package dev.flights.handlers.types;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public record FlightPriceAvailability(
        OptionalDouble newPrice,
        OptionalInt availableSeats
) {
}
