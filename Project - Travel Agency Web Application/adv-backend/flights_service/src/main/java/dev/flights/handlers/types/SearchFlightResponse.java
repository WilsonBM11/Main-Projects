package dev.flights.handlers.types;

public record SearchFlightResponse (
    String id,
    String airline,
    String departure,
    String destination,
    String departureTime,
    String arrivalTime,
    double firstClassPrice,
    double businessClassPrice,
    double commercialClassPrice
){}
