package dev.flights.database.repositories;

import dev.flights.database.documents.AirlineDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AirlinesRepository extends MongoRepository<AirlineDocument, String>{
}
