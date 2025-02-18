package dev.hotel_service.handler.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.AddressHotelsRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.exceptions.ErrorCodes;
import dev.hotel_service.exceptions.InvalidInputException;
import dev.hotel_service.handler.type.SearchHotelsResponse;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class SearchHotelsByAddressHandler {

    @Autowired
    AddressHotelsRepository addressHotelsRepository;

    public List<HotelEntity> searchHotelsByAddress(String address){
        Boolean authenticated = SessionContextHolder.getSession().authenticated();

        List<HotelEntity>hotels;
        if(authenticated){

            validateRequiredFields(address);
            hotels= addressHotelsRepository.findByAddressContainingIgnoreCase(address);

        }else{
            throw new BusinessException("Must to be logged", ErrorCodes.UNAUTHORIZED);
        }
        return hotels;
    }

    private void validateRequiredFields(String address) {
        if (address == null) {
            throw new BusinessException("The address is null", ErrorCodes.NULL_DATA);
        }
    }
}
