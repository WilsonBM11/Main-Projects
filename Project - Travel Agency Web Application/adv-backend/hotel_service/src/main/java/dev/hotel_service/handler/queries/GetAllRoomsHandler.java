package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllRoomsHandler {

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomEntity> getAllRooms () {
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        List<RoomEntity> rooms;

        if(authenticated){

            rooms = roomRepository.findAll();
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }
        return rooms;
    }

}
