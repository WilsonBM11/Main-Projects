package dev.hotel_service.handler.commands;

import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;

import java.util.List;
import java.util.UUID;

@Component
public class DeleteRoomHandler {

    @Autowired
    private RoomRepository roomRepository;

    SessionContextHolder sessionContextHolder = new SessionContextHolder();

    public void deleteRoom (UUID roomId){
        // Validate user permissions
        Session session = SessionContextHolder.getSession();
        sessionContextHolder.validateUserAdmin(session.roles());
        validateRequiredFields(roomId);

        roomRepository.deleteById(roomId);
    }

    private void validateRequiredFields(UUID roomId) {
        if (roomId == null) {
            throw new BusinessException("The room ID is null", ErrorCodes.NULL_DATA);
        }
    }

}
