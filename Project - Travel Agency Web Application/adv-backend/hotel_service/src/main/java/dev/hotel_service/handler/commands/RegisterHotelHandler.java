package dev.hotel_service.handler.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegisterHotelHandler {

    @Autowired
    private HotelRepository hotelRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public record Command(
            String name,
            Integer phoneNumber,
            String email,
            String address,
            List<String> facilities
    ) {}

    public void createHotel(RegisterHotelHandler.Command command){
        //Validación
        validateRequiredFields(command);

        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        sessionContextHolder.validateUserAdmin(session.roles());

        // Crear objeto Hotels
        HotelEntity hotel = new HotelEntity();
        hotel.setName(command.name());
        hotel.setPhoneNumber(command.phoneNumber());
        hotel.setEmail(command.email());
        hotel.setAddress(command.address());
        hotel.setFacilities(command.facilities());

        // Guardar hotel en la base de datos
        hotelRepository.save(hotel);
    }

    private void validateRequiredFields(RegisterHotelHandler.Command command) {
        if (command.name() == null || command.phoneNumber() == null || command.email() == null || command.address() == null || command.facilities() == null) {
            throw new BusinessException("Must complete all hotel data", ErrorCodes.NULL_DATA);
        }
    }

}
