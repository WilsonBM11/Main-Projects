package dev.flights.api.controllers;

import dev.flights.api.types.SearchByIdRequest;
import dev.flights.handlers.queries.SearchByIdQuery;
import dev.flights.handlers.types.SearchFlightDetailsResponse;
import dev.flights.handlers.types.SearchFlightResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchByIdController {

    @Autowired
    SearchByIdQuery query;

    @PostMapping(value = "/api/private/flights/searchById")
    public SearchFlightDetailsResponse searchById(@RequestBody SearchByIdRequest payload){
        return query.query(new SearchByIdQuery.Command(payload.id()));
    }
}
