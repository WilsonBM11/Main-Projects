package dev.hotel_service.api.controller;


import dev.hotel_service.api.types.SearchHotelsByAddressRequest;
import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.handler.queries.SearchHotelsByAddressHandler;
import dev.hotel_service.handler.type.SearchHotelsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressHotelsController {

    @Autowired
    SearchHotelsByAddressHandler searchHotelsByAddressHandler;

    @GetMapping("/api/private/hotels/searchByAddress/{address}")
    public List<HotelEntity> searchByAddress(@PathVariable("address") String address){
        return searchHotelsByAddressHandler.searchHotelsByAddress(address);
    }


}
