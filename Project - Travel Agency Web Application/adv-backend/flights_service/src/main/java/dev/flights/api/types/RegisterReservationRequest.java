package dev.flights.api.types;

import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.handlers.types.PassengerCompanion;

import java.util.List;
import java.util.Optional;

public record RegisterReservationRequest(
        String flightId,

        FlightSeatDocument.FlightClass flightClass,
        String passengerName,
        String dateOfBirth,

        String passengerEmail,

        String passengerTelephone,

        Optional<List<PassengerCompanion>> companions
) {


}
