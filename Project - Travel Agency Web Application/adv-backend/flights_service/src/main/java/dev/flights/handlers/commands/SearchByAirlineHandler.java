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
public class SearchByAirlineHandler {

    @Autowired
    FlightRepository repository;

    public record Command(
            String airline
    ){
    }

    public List<SearchFlightResponse> search(Command command){

        validateRequiredFields(command);

        List<SearchFlightResponse> flightResponses = new ArrayList<SearchFlightResponse>();

        List<FlightDocument> flights = repository.findAllByAirline(command.airline());

        for(FlightDocument flight : flights){

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
        if(command.airline() == null || command.airline().isEmpty()){
            throw new InvalidInputException("Airline is required");
        }
    }

}
