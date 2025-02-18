package dev.hotel_service.handler.commands;

import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;

import java.util.List;
import java.util.UUID;

@Component
public class DeleteReservationHandler {
    @Autowired
    private ReservationRepository reservationRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public void deleteReservation (UUID reservationId){
        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        sessionContextHolder.validateUserAdmin(session.roles());
        validateRequiredFields(reservationId);

        reservationRepository.deleteById(reservationId);
    }

    private void validateRequiredFields(UUID reservationId) {
        if (reservationId == null) {
            throw new BusinessException("The reservation ID is null", ErrorCodes.NULL_DATA);
        }
    }

}
