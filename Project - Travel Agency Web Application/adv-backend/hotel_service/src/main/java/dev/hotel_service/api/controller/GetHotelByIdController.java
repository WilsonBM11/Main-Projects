package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.handler.queries.GetHotelByIdHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class GetHotelByIdController {

    @Autowired
    private GetHotelByIdHandler getHotelByIdHandler;

    @GetMapping("/api/private/hotels/getHotelById/{hotelId}")
    public Optional<HotelEntity> getHotelById (@PathVariable("hotelId") UUID hotelId){
        return getHotelByIdHandler.getHotelById(hotelId);
    }
}
