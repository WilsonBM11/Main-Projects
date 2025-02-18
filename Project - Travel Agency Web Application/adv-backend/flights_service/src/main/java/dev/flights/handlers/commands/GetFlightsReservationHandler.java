package dev.flights.handlers.commands;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.handlers.types.FlightReservationResponse;
import dev.flights.handlers.types.FlightSeatResponse;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class GetFlightsReservationHandler {


    @Autowired
    FlightRepository flightRepository;


    public record Command(
            String id
    ){
    }

    public List<FlightReservationResponse> getFlightsReservation() {

        Session session = SessionContextHolder.getSession();

        Instant twentyFourHoursAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        Date twentyFourHoursAgoDate = Date.from(twentyFourHoursAgo);
        List<FlightReservationResponse> response = new ArrayList<>();
        List<FlightDocument> flights = flightRepository.findFlightsByPassengerId(session.getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (FlightDocument flight : flights) {
            System.out.println(flight.getDepartureTime());
            List<FlightSeatResponse> seatResponses = new ArrayList<>();

            if (flight.getDepartureTime().compareTo(twentyFourHoursAgoDate) >= 0) {

                for (FlightSeatDocument seat : flight.getSeats()) {

                    if (seat.getPassengerId().equals(session.getId())) {
                        System.out.println(seat.getState());
                        if(seat.getState().toString().equals(FlightSeatDocument.FlightState.RESERVED.toString())){

                            seatResponses.add(new FlightSeatResponse(
                                    seat.getSeatId(),
                                    seat.getPassengerId(),
                                    seat.getPassengerName(),
                                    seat.getFlightClass().toString(),
                                    seat.getPrice(),
                                    seat.getState().toString()
                            ));
                        }

                    }
                }
                if(seatResponses.size() != 0) {
                    response.add(new FlightReservationResponse(
                            flight.getId(),
                            flight.getAirline(),
                            flight.getDeparture(),
                            flight.getDestination(),
                            dateFormat.format(flight.getDepartureTime()),
                            dateFormat.format(flight.getArrivalTime()),
                            seatResponses
                    ));
                }
            }
        }

        return response;
    }


}
