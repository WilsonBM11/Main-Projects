package dev.hotel_service.handler.commands;


import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.UUID;

@Component
public class DeleteHotelHandler {
    @Autowired
    private HotelRepository hotelRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public void deleteHotel (UUID hotelId){
        // Validaciones
        Session session = SessionContextHolder.getSession();
        sessionContextHolder.validateUserAdmin(session.roles());
        validateRequiredFields(hotelId);

        hotelRepository.deleteById(hotelId);
    }


    private void validateRequiredFields(UUID hotelId) {
        if (hotelId == null) {
            throw new BusinessException("The hotel ID is null", ErrorCodes.NULL_DATA);
        }
    }
}
