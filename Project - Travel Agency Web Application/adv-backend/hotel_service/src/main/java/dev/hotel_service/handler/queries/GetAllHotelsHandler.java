package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetAllHotelsHandler {
    @Autowired
    private HotelRepository hotelRepository;

    public List<HotelEntity> getAllHotels () {
        Boolean authenticated = SessionContextHolder.getSession().authenticated();
        List<HotelEntity> hotels;

        if(authenticated){

            hotels = hotelRepository.findAll();
        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }

        return hotels;
    }
}
