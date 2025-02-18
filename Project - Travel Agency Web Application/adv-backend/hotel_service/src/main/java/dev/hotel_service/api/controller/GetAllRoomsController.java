package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.handler.queries.GetAllRoomsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAllRoomsController {

    @Autowired
    private GetAllRoomsHandler getAllRoomsHandler;

    @GetMapping(value = "/api/private/hotels/getRooms")
    public List<RoomEntity> getAllRooms () {
        return getAllRoomsHandler.getAllRooms();
    }
}
