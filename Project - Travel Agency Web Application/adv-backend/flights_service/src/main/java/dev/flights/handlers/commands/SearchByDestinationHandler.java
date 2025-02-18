package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.types.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchByDestinationHandler {
    @Autowired
    FlightRepository repository;

    public record Command(
            String destination
    ){
    }

    public List<SearchFlightResponse> search(Command command){
        // Validations
        validateRequiredFields(command);

        List<SearchFlightResponse> flightResponses = new ArrayList<SearchFlightResponse>();

        // Search Flight Data
        List<FlightDocument> flights = repository.findAllByDestination(command.destination());

        for (FlightDocument flight : flights) {

            SearchFlightResponse response = new SearchFlightResponse(
                    flight.getId(),
                    flight.getAirline(),
                    flight.getDeparture(),
                    flight.getDestination(),
                    flight.getDepartureTime().toString(),
                    flight.getArrivalTime().toString(),
                    flight.getFirstClassPrice(),
                    flight.getBusinessClassPrice(),
                    flight.getCommercialClassPrice()
            );

            flightResponses.add(response);
        }

        return flightResponses;
    }

    private void validateRequiredFields(Command command){
        if(command.destination() == null || command.destination().isEmpty()){
            throw new InvalidInputException("Destination is required");
        }
    }
}
