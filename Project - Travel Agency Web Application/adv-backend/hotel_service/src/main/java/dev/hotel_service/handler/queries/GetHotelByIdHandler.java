package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.HotelRepository;
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
public class GetHotelByIdHandler {

    @Autowired
    private HotelRepository hotelRepository;

    public Optional<HotelEntity> getHotelById(UUID hotelId){
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        Optional<HotelEntity> hotel;

        if(authenticated){
            validateRequiredFields(hotelId);
            hotel = hotelRepository.findById(hotelId);
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }

        return hotel;
    }

    private void validateRequiredFields(UUID hotelId) {
        if (hotelId == null) {
            throw new BusinessException("The hotel ID is null", ErrorCodes.NULL_DATA);
        }
    }

}
