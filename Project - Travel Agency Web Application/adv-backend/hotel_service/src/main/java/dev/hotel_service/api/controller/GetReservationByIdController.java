package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.handler.queries.GetReservationByIdHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class GetReservationByIdController {

    @Autowired
    private GetReservationByIdHandler getReservationByIdHandler;
    @GetMapping("/api/private/hotels/getReservationById/{reservationId}")
    public Optional<RoomReservationEntity> getReservationById (@PathVariable("reservationId") UUID reservationId){
        return getReservationByIdHandler.getReservationById(reservationId);
    }
}
