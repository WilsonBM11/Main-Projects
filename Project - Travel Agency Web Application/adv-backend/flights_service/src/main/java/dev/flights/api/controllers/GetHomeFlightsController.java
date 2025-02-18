package dev.flights.api.controllers;

import dev.flights.handlers.queries.GetFlightsHomepageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetHomeFlightsController {

    @Autowired
    GetFlightsHomepageQuery query;

    @GetMapping(value = "/api/private/flights/home")
    public Object getHomeFlights(){
        return query.handle();
    }

}
