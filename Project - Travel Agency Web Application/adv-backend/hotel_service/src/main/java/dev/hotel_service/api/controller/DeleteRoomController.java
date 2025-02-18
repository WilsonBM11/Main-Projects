package dev.hotel_service.api.controller;

import dev.hotel_service.handler.commands.DeleteRoomHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DeleteRoomController {

    @Autowired
    private DeleteRoomHandler deleteRoomHandler;
    @DeleteMapping("/api/private/hotels/deleteRoom/{room_id}")
    public String deleteRoom (@PathVariable("room_id") UUID roomId){

        deleteRoomHandler.deleteRoom(roomId);
        return "OK";
    }

}
