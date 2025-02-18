package dev.flights.database.repositories;

import dev.flights.database.documents.FlightDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FlightRepository extends MongoRepository<FlightDocument, String> {
    List<FlightDocument> findAllByAirline(String airline);
    List<FlightDocument> findAllByDestination(String destination);
    @Query("{ 'seats.passengerId': ?0 }")
    List<FlightDocument> findFlightsByPassengerId(String passengerId);

    @Query("{ 'seats.seatId': ?0}")
    Optional<FlightDocument> findFlightBySeatId(String seatId);



}

