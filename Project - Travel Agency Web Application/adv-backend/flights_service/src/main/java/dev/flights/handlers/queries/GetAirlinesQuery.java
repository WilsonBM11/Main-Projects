package dev.flights.handlers.queries;

import dev.flights.database.documents.AirlineDocument;
import dev.flights.database.repositories.AirlinesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class GetAirlinesQuery {

    @Autowired
    AirlinesRepository repository;

    public List<String> handle() {
        return repository.findAll().stream()
                .map(AirlineDocument::getAirline)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

}
