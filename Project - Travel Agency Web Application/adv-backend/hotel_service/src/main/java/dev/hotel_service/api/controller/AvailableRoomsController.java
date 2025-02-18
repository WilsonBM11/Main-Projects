package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.handler.queries.AvailableRoomsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class AvailableRoomsController {
    @Autowired
    AvailableRoomsHandler availableRoomsHandler;

    @GetMapping("/api/private/hotels/getRooms/{hotelId}")
    public List<RoomEntity> getAvailableRoomsByHotel(@PathVariable UUID hotelId) {
        return availableRoomsHandler.getAvailableRoomsByHotel(hotelId);

    }
}
