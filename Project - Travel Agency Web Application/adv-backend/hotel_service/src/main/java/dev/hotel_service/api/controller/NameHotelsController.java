package dev.hotel_service.api.controller;

import dev.hotel_service.api.types.SearchHotelsByNameRequest;
import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.handler.queries.SearchHotelsByNameHandler;
import dev.hotel_service.handler.type.SearchHotelsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class NameHotelsController {

    @Autowired
    SearchHotelsByNameHandler searchHotelsByNameHandler;

    @GetMapping("/api/private/hotels/searchByName/{name}")
    public List<HotelEntity> searchByName(@PathVariable("name") String name){
        return searchHotelsByNameHandler.searchHotelsByName(name);
    }
}
