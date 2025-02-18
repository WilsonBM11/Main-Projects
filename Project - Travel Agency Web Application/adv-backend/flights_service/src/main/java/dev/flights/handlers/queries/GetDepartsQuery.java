package dev.flights.handlers.queries;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class GetDepartsQuery {

    @Autowired
    FlightRepository repository;


    public List<String> handle() {
        return repository.findAll().stream().map(FlightDocument::getDeparture).distinct().toList();
    }
}
