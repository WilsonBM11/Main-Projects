package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.types.FlightPriceAvailability;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class ChangeDetailsFlightHandler {

    @Autowired
    FlightRepository repository;

    public record Command(
            String flightId,
            Optional<FlightPriceAvailability> firstClass,
            Optional<FlightPriceAvailability> businessClass,
            Optional<FlightPriceAvailability> commercialClass
    ) {
    }

    public String handle(Command command){

        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        validateUserAdmin(session.roles());

        // Validate classes data
        validateFields(command);

        Optional<FlightDocument> flight = repository.findById(command.flightId());

        validateFlightExist(flight);

        // Change flight data
        changeFlightData(command, flight.get());

        // Update flight data
        repository.save(flight.get());

        return "OK";
    }

    private void changeFlightData(Command command, FlightDocument flight){
        changeFlightPriceAndAvailability(command.firstClass, flight::setAvailableFirstClassSeats, flight::setFirstClassPrice);
        changeFlightPriceAndAvailability(command.businessClass, flight::setAvailableBusinessClassSeats, flight::setBusinessClassPrice);
        changeFlightPriceAndAvailability(command.commercialClass, flight::setAvailableCommercialClassSeats, flight::setCommercialClassPrice);
    }

    private void changeFlightPriceAndAvailability(Optional<FlightPriceAvailability> classData, Consumer<Integer> setAvailableSeats, Consumer<Double> setPrice) {
        classData.ifPresent(c -> {
            c.availableSeats().ifPresent(setAvailableSeats::accept);
            c.newPrice().ifPresent(setPrice::accept);
        });
    }

    private void validateUserAdmin(List<String> roles) {
        boolean hasAdminRole = roles.stream()
                .anyMatch(role -> role.equals("ADMIN"));
        if (!hasAdminRole){
            throw new BusinessException("The user cannot update flights prices or seats availability", 403);
        }
    }

    private void validateFlightExist(Optional<FlightDocument> flight){
        if(flight.isEmpty()){
            throw new BusinessException("This flight does not exist",400);
        }
    }

    private void validateFields(Command command){
        if (isNullOrEmpty(command.flightId())) {
            throw new BusinessException("Flight ID is required.", 400);
        }

        validateClassData(command.firstClass(), "First Class");
        validateClassData(command.businessClass(), "Business Class");
        validateClassData(command.commercialClass(), "Commercial Class");
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }

    private void validateClassData(Optional<FlightPriceAvailability> classData, String classType){
        if (classData.isPresent()) {
            classData.ifPresent(c -> {
                c.availableSeats().ifPresent(seats -> {
                    if (seats <= 0)
                        throw new BusinessException("Invalid available seats value for " + classType, 400);
                });
                c.newPrice().ifPresent(price -> {
                    if (price <= 0)
                        throw new BusinessException("Invalid seat price value for " + classType, 400);
                });
            });
        }
    }
}
