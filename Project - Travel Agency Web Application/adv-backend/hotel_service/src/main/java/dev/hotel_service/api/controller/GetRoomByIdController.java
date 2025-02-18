package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.handler.queries.GetRoomByIdHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
public class GetRoomByIdController {
    @Autowired
    GetRoomByIdHandler getRoomByIdHandler;

    @GetMapping("/api/private/hotels/getRoomById/{roomId}")
    public Optional<RoomEntity> getRoomById (@PathVariable("roomId") UUID roomId){
        return getRoomByIdHandler.getRoomById(roomId);
    }
}
