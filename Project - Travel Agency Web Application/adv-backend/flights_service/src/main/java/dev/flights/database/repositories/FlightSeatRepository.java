package dev.flights.database.repositories;

import dev.flights.database.documents.FlightSeatDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FlightSeatRepository extends MongoRepository<FlightSeatDocument, String>{
}
