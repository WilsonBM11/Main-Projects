package dev.flights.api.controllers;

import dev.flights.api.types.ChangeDetailsFlightRequest;
import dev.flights.handlers.commands.ChangeDetailsFlightHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/flights")
public class ChangeDetailsFlightController {

    @Autowired
    ChangeDetailsFlightHandler changeDetailsFlightHandler;

    @PutMapping(value = "/changeDetailsFlight")
    public String changeDetailsFlight(@RequestBody ChangeDetailsFlightRequest payload){
        return changeDetailsFlightHandler.handle(new ChangeDetailsFlightHandler.Command(
                payload.flightId(),
                payload.firstClass(),
                payload.businessClass(),
                payload.commercialClass()
        ));
    }
}
