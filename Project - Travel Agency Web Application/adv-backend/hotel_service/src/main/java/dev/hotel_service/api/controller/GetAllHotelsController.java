package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.handler.queries.GetAllHotelsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAllHotelsController {

    @Autowired
    private GetAllHotelsHandler getAllHotelsHandler;

    @GetMapping(value = "/api/private/hotels/getHotels")
    public List<HotelEntity> getAllHotels () {
        return getAllHotelsHandler.getAllHotels();
    }
}
