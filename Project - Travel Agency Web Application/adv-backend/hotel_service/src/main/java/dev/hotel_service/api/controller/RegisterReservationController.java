package dev.hotel_service.api.controller;

import dev.hotel_service.api.types.ReservationControllerRequest;
import dev.hotel_service.handler.commands.RegisterReservationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterReservationController {

    @Autowired
    RegisterReservationHandler registerReservationHandler;

    @PostMapping(value = "/api/private/hotels/registerReservation")
    public String registerReservation(@RequestBody ReservationControllerRequest payload) {
        registerReservationHandler.registerReservation(new RegisterReservationHandler.Command(
                payload.startDate(),
                payload.endDate(),
                payload.userEmail(),
                payload.roomId()
        ));
        return "OK";
    }
}
