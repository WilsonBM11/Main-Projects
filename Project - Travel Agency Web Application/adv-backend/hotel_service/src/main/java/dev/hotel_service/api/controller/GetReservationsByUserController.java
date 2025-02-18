package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.GetReservationsByUserHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class GetReservationsByUserController {
    @Autowired
    GetReservationsByUserHandler getReservationsByUserHandler;

    @GetMapping("/api/private/hotels/getReservationsByUser")
    public List<RoomReservationEntity> getReservationsByUser() {
        return getReservationsByUserHandler.getReservationsByUser();

    }
}
