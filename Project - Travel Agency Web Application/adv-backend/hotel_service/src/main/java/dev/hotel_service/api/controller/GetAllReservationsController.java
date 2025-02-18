package dev.hotel_service.api.controller;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.handler.queries.GetAllReservationsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAllReservationsController {

    @Autowired
    private GetAllReservationsHandler getAllReservationsHandler;

    @GetMapping(value = "/api/private/hotels/getReservations")
    public List<RoomReservationEntity> getAllReservations () {
        return getAllReservationsHandler.getAllReservations();
    }

}
