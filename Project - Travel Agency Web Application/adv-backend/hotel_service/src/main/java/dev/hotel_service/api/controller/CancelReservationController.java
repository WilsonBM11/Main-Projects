package dev.hotel_service.api.controller;

import dev.hotel_service.handler.commands.CancelReservationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CancelReservationController {
    @Autowired
    CancelReservationHandler cancelReservationHandler;

    @PostMapping(value = "/api/private/hotels/cancelReservation/{reservationId}")
    public String cancelReservation(@PathVariable("reservationId") UUID reservationId) {
        cancelReservationHandler.cancelReservation(reservationId);
        return "OK";
    }
}
