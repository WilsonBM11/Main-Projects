package dev.flights.api.controllers;

import dev.flights.handlers.queries.GetDestiniesQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetDestiniesController {
    @Autowired
    GetDestiniesQuery handler;

    @GetMapping(value = "/api/private/flights/getDestinies")
    public List<String> getDestinies(){
        return handler.handle();
    }
}
