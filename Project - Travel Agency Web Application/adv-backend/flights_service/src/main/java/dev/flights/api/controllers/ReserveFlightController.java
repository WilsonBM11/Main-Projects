package dev.flights.api.controllers;


import dev.flights.api.types.RegisterReservationRequest;
import dev.flights.handlers.commands.RegisterFlightReservationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReserveFlightController {

    @Autowired
    RegisterFlightReservationHandler handler;

    @PostMapping(value = "/api/private/flights/reserveFlight")
    public String reserve(@RequestBody RegisterReservationRequest payload){

        handler.handle(new RegisterFlightReservationHandler.Command(
                payload.flightId(),
                payload.flightClass(),
                payload.passengerName(),
                payload.dateOfBirth(),
                payload.passengerEmail(),
                payload.passengerTelephone(),
                payload.companions()
        ));

        return "OK";
    }
}
