package dev.hotel_service.api.controller;

import dev.hotel_service.handler.commands.DeleteHotelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DeleteHotelController {

    @Autowired
    private DeleteHotelHandler deleteHotelHandler;
    @DeleteMapping("/api/private/hotels/deleteHotel/{hotel_id}")
    public String deleteHotel (@PathVariable("hotel_id") UUID hotelId){

        deleteHotelHandler.deleteHotel(hotelId);
        return "OK";
    }

}
