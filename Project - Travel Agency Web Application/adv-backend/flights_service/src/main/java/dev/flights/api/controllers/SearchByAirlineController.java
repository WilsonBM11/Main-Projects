package dev.flights.api.controllers;


import dev.flights.api.types.SearchByAirlineRequest;
import dev.flights.handlers.commands.SearchByAirlineHandler;
import dev.flights.handlers.types.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchByAirlineController {

    @Autowired
    SearchByAirlineHandler handler;

    @PostMapping(value = "/api/private/flights/searchByAirline")
    public List<SearchFlightResponse> searchByAirline(@RequestBody SearchByAirlineRequest payload){
        System.out.println(payload.airline());
        return handler.search(new SearchByAirlineHandler.Command(payload.airline()));
    }

}
