package dev.hotel_service.api.controller;

import dev.hotel_service.handler.commands.DeleteReservationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class DeleteReservationController {

    @Autowired
    private DeleteReservationHandler deleteReservationHandler;

    @DeleteMapping("/api/private/hotels/deleteReservation/{reservation_id}")
    public String deleteReservation (@PathVariable("reservation_id") UUID reservationId){
        deleteReservationHandler.deleteReservation(reservationId);

        return "OK";
    }
}
