package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.AvailableRoomsRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AvailableRoomsHandler {

        @Autowired
        AvailableRoomsRepository availableRoomsRepository;

        public List<RoomEntity> getAvailableRoomsByHotel(UUID hotelId) {
            Boolean authenticated = SessionContextHolder.getSession().authenticated();
            List<RoomEntity> availableRooms;

            if(authenticated){
                validateRequiredFields(hotelId);

                availableRooms = availableRoomsRepository.findByAvailableTrueAndHotelId_HotelId(hotelId);
            }else{
                throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
            }

            return  availableRooms;
        }

    private void validateRequiredFields(UUID hotelId) {
        if (hotelId == null) {
            throw new BusinessException("The hotel ID is null", ErrorCodes.NULL_DATA);
        }
    }


}
