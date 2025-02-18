package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.NameHotelsRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.exceptions.InvalidInputException;
import dev.hotel_service.handler.type.SearchHotelsResponse;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SearchHotelsByNameHandler {

    @Autowired
    NameHotelsRepository nameHotelsRepository;

    public List<HotelEntity> searchHotelsByName(String name){
        Boolean authenticated = SessionContextHolder.getSession().authenticated();

        List<HotelEntity> hotels;
        if(authenticated){
            validateRequiredFields(name);
            hotels=nameHotelsRepository.findByNameContainingIgnoreCase(name);

        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }
        return hotels;
    }

    private void validateRequiredFields(String name) {
        if (name == null) {
            throw new BusinessException("The name hotel is null", ErrorCodes.NULL_DATA);
        }
    }
}
