package dev.flights.handlers.commands;

import dev.flights.database.documents.AirlineDocument;
import dev.flights.database.repositories.AirlinesRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class RegisterAirlineHandler {

    @Autowired
    AirlinesRepository repository;

    public record Command(
            String airline
    ){
    }

    public void register(Command command){
        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        validateUserAdmin(session.roles());
        // Validations
        validateRequiredFields(command);
        AirlineDocument airline = new AirlineDocument();
        airline.setId(UUID.randomUUID().toString());
        airline.setAirline(command.airline());

        repository.save(airline);
    }

    private void validateUserAdmin(List<String> roles) {
        boolean hasAdminRole = roles.stream()
                .anyMatch(role -> role.equals("ADMIN"));
        if (!hasAdminRole){
            throw new BusinessException("The user cannot add flights", 403);
        }
    }

    private void validateRequiredFields(Command command){
        if(isNullOrEmpty(command.airline()) ){
            throw new BusinessException("Must complete all flight data", 400);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }

}
