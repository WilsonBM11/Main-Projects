package dev.hotel_service.api.controller;

import dev.hotel_service.api.types.RoomControllerRequest;
import dev.hotel_service.handler.commands.RegisterRoomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterRoomController {

    @Autowired
    RegisterRoomHandler registerRoomHandler;

    @PostMapping(value = "/api/private/hotels/registerRoom")
    public String registerRoom(@RequestBody RoomControllerRequest payload) {
        registerRoomHandler.registerRoom(new RegisterRoomHandler.Command(
                payload.roomNumber(),
                payload.details(),
                payload.price(),
                payload.hotelId()
        ));
        return "OK";
    }
}
