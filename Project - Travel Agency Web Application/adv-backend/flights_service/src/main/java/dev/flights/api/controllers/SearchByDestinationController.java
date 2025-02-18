package dev.flights.api.controllers;

import dev.flights.api.types.SearchByDestinationRequest;
import dev.flights.handlers.commands.SearchByDestinationHandler;
import dev.flights.handlers.types.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchByDestinationController {

    @Autowired
    SearchByDestinationHandler handler;

    @PostMapping(value = "/api/private/flights/searchByDestination")
    public List<SearchFlightResponse> searchByDestination(@RequestBody SearchByDestinationRequest payload){
        System.out.println(payload);
        return handler.search(new SearchByDestinationHandler.Command(payload.destination()));
    }
}
