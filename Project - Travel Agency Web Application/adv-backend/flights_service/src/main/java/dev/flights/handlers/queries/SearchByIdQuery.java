package dev.flights.handlers.queries;

import com.mongodb.internal.session.SessionContext;
import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.types.SearchFlightDetailsResponse;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SearchByIdQuery {

    @Autowired
    FlightRepository repository;

    public record Command(
            String id
    ){
    }

    public SearchFlightDetailsResponse query(Command command) {
        validateRequiredField(command);
        SessionContextHolder.getSession();
        Optional<FlightDocument> flight = repository.findById(command.id());
        validateFlightExists(flight);
        return new SearchFlightDetailsResponse(
                flight.orElseThrow().getId(),
                flight.orElseThrow().getAirline(),
                flight.orElseThrow().getDeparture(),
                flight.orElseThrow().getDestination(),
                flight.orElseThrow().getDepartureTime().toString(),
                flight.orElseThrow().getArrivalTime().toString(),
                flight.orElseThrow().getFirstClassPrice(),
                flight.orElseThrow().getBusinessClassPrice(),
                flight.orElseThrow().getCommercialClassPrice(),
                flight.orElseThrow().getAvailableFirstClassSeats(),
                flight.orElseThrow().getAvailableBusinessClassSeats(),
                flight.orElseThrow().getAvailableCommercialClassSeats()
        );

    }

    private void validateRequiredField(Command command){
        if(command.id() == null || command.id().isEmpty()){
            throw new InvalidInputException("The id of the flight is required");
        }
    }

    private void validateFlightExists(Optional<FlightDocument> flight){
        if(flight.isPresent() == false){
            throw new BusinessException("There is no flight that match the request",400);
        }
    }
}
