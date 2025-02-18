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
import java.util.Optional;
import java.util.UUID;

@Component
public class GetRoomByIdHandler {
    @Autowired
    private RoomRepository roomRepository;

    public Optional<RoomEntity> getRoomById (UUID roomId){
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        Optional<RoomEntity> room;

        if(authenticated){
            validateRequiredFields(roomId);
            room = roomRepository.findById(roomId);
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }

        return room;
    }

    private void validateRequiredFields(UUID roomId) {
        if (roomId == null) {
            throw new BusinessException("The room ID is null", ErrorCodes.NULL_DATA);
        }
    }

}
