package dev.flights.api.controllers;

import dev.flights.api.types.RegisterFlightRequest;
import dev.flights.handlers.commands.RegisterFlightHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterFlightController {

   @Autowired
   RegisterFlightHandler handler;

   @PostMapping(value = "/api/private/flights/flight")
   public String register(@RequestBody RegisterFlightRequest payload){
       handler.register(new RegisterFlightHandler.Command(
               payload.airline(),
               payload.departure(),
               payload.destination(),
               payload.departureTime(),
               payload.arrivalTime(),
               payload.firstClassSeats(),
               payload.businessClassSeats(),
               payload.commercialClassSeats(),
               payload.firstClassPrice(),
               payload.businessClassPrice(),
               payload.commercialClassPrice()
       ));
       return "OK";
   }

}
