package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class RegisterFlightHandler {

    @Autowired
    FlightRepository repository;

    public record Command(
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
    ){
    }

    public void register(Command command){
        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        validateUserAdmin(session.roles());

        // Validations
        validateRequiredFields(command);

        // Save Flight Data
        FlightDocument flight = new FlightDocument();
        flight.setId(UUID.randomUUID().toString());
        flight.setAirline(command.airline());
        flight.setDeparture(command.departure());
        flight.setDestination(command.destination());
        flight.setDepartureTime(setDate(command.departureTime()));
        flight.setArrivalTime(setDate(command.arrivalTime()));
        flight.setFirstClassSeats(command.firstClassSeats());
        flight.setBusinessClassSeats(command.businessClassSeats());
        flight.setCommercialClassSeats(command.commercialClassSeats());
        flight.setAvailableFirstClassSeats(command.firstClassSeats());
        flight.setAvailableBusinessClassSeats(command.businessClassSeats());
        flight.setAvailableCommercialClassSeats(command.commercialClassSeats());
        flight.setFirstClassPrice(command.firstClassPrice());
        flight.setBusinessClassPrice(command.businessClassPrice());
        flight.setCommercialClassPrice(command.commercialClassPrice());

        // Create an Empty List to Save the Flight Seats
        List<FlightSeatDocument> emptySeats = new ArrayList<>();
        flight.setSeats(emptySeats);

        repository.save(flight);
    }

    private void validateUserAdmin(List<String> roles) {
        boolean hasAdminRole = roles.stream()
                .anyMatch(role -> role.equals("ADMIN"));
        if (!hasAdminRole){
            throw new BusinessException("The user cannot add flights", 403);
        }
    }

    private void validateRequiredFields(Command command){
        if(isNullOrEmpty(command.airline) || isNullOrEmpty(command.departure) || isNullOrEmpty(command.destination()) || isNullOrEmpty(command.departureTime()) || isNullOrEmpty(command.arrivalTime())){
            throw new BusinessException("Must complete all flight data", 400);
        }

        if(invalidNumberValue(command.firstClassSeats) || invalidNumberValue(command.firstClassPrice) || invalidNumberValue(command.businessClassSeats) || invalidNumberValue(command.businessClassPrice) || invalidNumberValue(command.commercialClassSeats) || invalidNumberValue(command.commercialClassPrice)){
            throw new BusinessException("Invalid number value", 400);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }

    private boolean invalidNumberValue(double value) { return value <= 0;}

    private Date setDate(String time){

        String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

        try {
            return formatter.parse(time);
        }catch (Exception e){
            throw new BusinessException("Introduce a valid date", 400);
        }
    }
}