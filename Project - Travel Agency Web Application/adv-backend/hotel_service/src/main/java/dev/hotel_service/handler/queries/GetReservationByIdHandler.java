package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetReservationByIdHandler {

    @Autowired
    private ReservationRepository reservationRepository;

    public Optional<RoomReservationEntity> getReservationById (UUID reservationId){
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        Optional<RoomReservationEntity> reservation;

        if(authenticated){
            validateRequiredFields(reservationId);
            reservation = reservationRepository.findById(reservationId);
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }

        return reservation;
    }

    private void validateRequiredFields(UUID reservationId) {
        if (reservationId == null) {
            throw new BusinessException("The reservation ID is null", ErrorCodes.NULL_DATA);
        }
    }
}
