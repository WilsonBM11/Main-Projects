package dev.flights.api.controllers;

import dev.flights.api.types.CancelFlightRequest;
import dev.flights.handlers.commands.CancelFlightHandler;
import dev.flights.handlers.types.FlightReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/flights")
public class CancelFlightController {

    @Autowired
    CancelFlightHandler cancelFlightHandler;

    @PostMapping(value = "/cancelFlight")
    public String cancelFlight(@RequestBody CancelFlightRequest payload){
        cancelFlightHandler.cancelFlight(new CancelFlightHandler.Command(payload.seatId()));
        return "OK";
    }
}
