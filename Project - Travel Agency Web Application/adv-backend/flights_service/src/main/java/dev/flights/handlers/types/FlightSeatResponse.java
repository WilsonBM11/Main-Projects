package dev.flights.handlers.types;

public record FlightSeatResponse(
        String seatId,
        String passengerId,
        String passengerName,
        String seatClass,
        double seatPrice,
        String seatState

) {
}
