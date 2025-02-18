package dev.hotel_service.api.controller;

import dev.hotel_service.api.types.HotelControllerRequest;
import dev.hotel_service.handler.commands.RegisterHotelHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterHotelController {
    @Autowired
    RegisterHotelHandler hotelHandler;

    @PostMapping(value = "api/private/hotels/registerHotel")
    public String registerHotel(@RequestBody HotelControllerRequest payload) {
        hotelHandler.createHotel(new RegisterHotelHandler.Command(
                payload.name(),
                payload.phoneNumber(),
                payload.email(),
                payload.address(),
                payload.facilities()
        ));
        return "OK";
    }
}
