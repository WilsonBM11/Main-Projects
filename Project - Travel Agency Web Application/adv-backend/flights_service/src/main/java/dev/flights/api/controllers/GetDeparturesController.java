package dev.flights.api.controllers;

import dev.flights.handlers.queries.GetDepartsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetDeparturesController {
    @Autowired
    GetDepartsQuery handler;

    @GetMapping(value = "/api/private/flights/getDepartures")
    public List<String> getDepartures(){
        return handler.handle();
    }
}
