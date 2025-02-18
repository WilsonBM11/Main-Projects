package dev.flights.api.controllers;


import dev.flights.api.types.RegisterAirlineRequest;
import dev.flights.handlers.commands.RegisterAirlineHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateAirlineController {

    @Autowired
    RegisterAirlineHandler handler;

    @PostMapping(value = "/api/private/flights/registerAirline")
    public String register(@RequestBody RegisterAirlineRequest payload){
        handler.register(new RegisterAirlineHandler.Command(
                payload.airline()
        ));
        return "OK";
    }
}
