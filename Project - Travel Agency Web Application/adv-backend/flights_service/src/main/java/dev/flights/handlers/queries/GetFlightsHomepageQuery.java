package dev.flights.handlers.queries;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.handlers.types.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class GetFlightsHomepageQuery {

    @Autowired
    FlightRepository repository;

    public List<SearchFlightResponse> handle() {
        List<SearchFlightResponse> responses = repository.findAll().stream()
                .map(this::toSearchFlightResponse)
                .distinct()
                .collect(Collectors.toList());

        // Mezcla la lista para seleccionar elementos aleatorios
        Collections.shuffle(responses, new Random());

        // Retorna los primeros 8 elementos de la lista mezclada
        return responses.stream().limit(8).collect(Collectors.toList());
    }

    private SearchFlightResponse toSearchFlightResponse(FlightDocument document) {

        return new SearchFlightResponse(
                document.getId(),
                document.getAirline(),
                document.getDeparture(),
                document.getDestination(),
                document.getDepartureTime().toString(),
                document.getArrivalTime().toString(),
                document.getFirstClassPrice(),
                document.getBusinessClassPrice(),
                document.getCommercialClassPrice()
        );
    }

}
