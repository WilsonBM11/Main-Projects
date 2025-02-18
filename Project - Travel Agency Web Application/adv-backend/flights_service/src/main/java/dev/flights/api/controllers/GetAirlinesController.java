package dev.flights.api.controllers;

import dev.flights.handlers.queries.GetAirlinesQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetAirlinesController {
    @Autowired
    GetAirlinesQuery handler;

    @GetMapping(value = "/api/private/flights/getAirlines")
    public List<String> getAirlines(){
        return handler.handle();
    }

}
