package dev.flights.api.types;

public record RegisterFlightRequest (
    String airline,
    String departure,
    String destination,
    String departureTime,
    String arrivalTime,
    int firstClassSeats,
    int businessClassSeats,
    int commercialClassSeats,
    double firstClassPrice,
    double businessClassPrice,
    double commercialClassPrice
){}
