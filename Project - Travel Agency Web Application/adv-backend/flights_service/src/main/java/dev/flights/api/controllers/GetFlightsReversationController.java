package dev.flights.api.controllers;

import dev.flights.handlers.commands.GetFlightsReservationHandler;
import dev.flights.handlers.types.FlightReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetFlightsReversationController {

    @Autowired
    GetFlightsReservationHandler handler;

    @GetMapping(value = "/api/private/flights/GetFlightsReservation")
    public List<FlightReservationResponse> getFlightsReservation(){
        List<FlightReservationResponse> response =  handler.getFlightsReservation();
        return response;
    }

}
