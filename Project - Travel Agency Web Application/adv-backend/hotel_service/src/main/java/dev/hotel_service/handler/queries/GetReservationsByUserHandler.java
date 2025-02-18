package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationsByUserRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class GetReservationsByUserHandler {
    @Autowired
    ReservationsByUserRepository reservationsByUserRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public List<RoomReservationEntity> getReservationsByUser() {
        // Recuperar id del usuario
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        List<RoomReservationEntity> reservations;

        if(authenticated){
            Session session = SessionContextHolder.getSession();
            reservations = reservationsByUserRepository.findReservationsByUserId(sessionContextHolder.getActualUser());
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }

        return reservations;
    }

}
