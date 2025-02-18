package handlers;
import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.handlers.commands.GetFlightsReservationHandler;
import dev.flights.handlers.types.FlightReservationResponse;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GetFlightReservationHandlerTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private GetFlightsReservationHandler getFlightsReservationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetFlightsReservationSuccess() {
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId("1A");
            seat.setPassengerId("passenger123");
            seat.setPassengerName("John Doe");
            seat.setFlightClass(FlightSeatDocument.FlightClass.BUSINESS);
            seat.setPrice(200.0);
            seat.setState(FlightSeatDocument.FlightState.RESERVED);

            List<FlightSeatDocument> seats = new ArrayList<>();
            seats.add(seat);

            FlightDocument flight = new FlightDocument();
            flight.setId("FL123");
            flight.setAirline("Airline");
            flight.setDeparture("CityA");
            flight.setDestination("CityB");
            flight.setDepartureTime(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)));
            flight.setArrivalTime(Date.from(Instant.now().plus(5, ChronoUnit.HOURS)));
            flight.setSeats(seats);

            List<FlightDocument> flights = new ArrayList<>();
            flights.add(flight);

            when(flightRepository.findFlightsByPassengerId("passenger123")).thenReturn(flights);

            List<FlightReservationResponse> result = getFlightsReservationHandler.getFlightsReservation();

            assertEquals(1, result.size());
            assertEquals("FL123", result.get(0).id());
            assertEquals(1, result.get(0).seats().size());
            assertEquals("1A", result.get(0).seats().get(0).seatId());
        }
    }

    @Test
    void testGetFlightsReservationFlight24HoursAgo() {
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId("1A");
            seat.setPassengerId("passenger123");
            seat.setPassengerName("John Doe");
            seat.setFlightClass(FlightSeatDocument.FlightClass.BUSINESS);
            seat.setPrice(200.0);
            seat.setState(FlightSeatDocument.FlightState.RESERVED);

            List<FlightSeatDocument> seats = new ArrayList<>();
            seats.add(seat);

            FlightDocument flight = new FlightDocument();
            flight.setId("FL123");
            flight.setAirline("Airline");
            flight.setDeparture("CityA");
            flight.setDestination("CityB");
            flight.setDepartureTime(Date.from(Instant.now().minus(25, ChronoUnit.HOURS)));
            flight.setArrivalTime(Date.from(Instant.now().minus(22, ChronoUnit.HOURS)));
            flight.setSeats(seats);

            List<FlightDocument> flights = new ArrayList<>();
            flights.add(flight);

            when(flightRepository.findFlightsByPassengerId("passenger123")).thenReturn(flights);

            List<FlightReservationResponse> result = getFlightsReservationHandler.getFlightsReservation();

            assertEquals(0, result.size());
        }
    }

    @Test
    void testGetFlightsReservationDifferentPassengerId() {
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId("1A");
            seat.setPassengerId("456");
            seat.setPassengerName("John Doe");
            seat.setFlightClass(FlightSeatDocument.FlightClass.BUSINESS);
            seat.setPrice(200.0);
            seat.setState(FlightSeatDocument.FlightState.RESERVED);

            List<FlightSeatDocument> seats = new ArrayList<>();
            seats.add(seat);

            FlightDocument flight = new FlightDocument();
            flight.setId("FL123");
            flight.setAirline("Airline");
            flight.setDeparture("CityA");
            flight.setDestination("CityB");
            flight.setDepartureTime(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)));
            flight.setArrivalTime(Date.from(Instant.now().plus(5, ChronoUnit.HOURS)));
            flight.setSeats(seats);

            List<FlightDocument> flights = new ArrayList<>();
            flights.add(flight);

            when(flightRepository.findFlightsByPassengerId("passenger123")).thenReturn(flights);

            List<FlightReservationResponse> result = getFlightsReservationHandler.getFlightsReservation();

            assertEquals(0, result.size());
        }
    }

    @Test
    void testGetFlightsReservationAllSeatsCancelled() {
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId("1A");
            seat.setPassengerId("passenger123");
            seat.setPassengerName("John Doe");
            seat.setFlightClass(FlightSeatDocument.FlightClass.BUSINESS);
            seat.setPrice(200.0);
            seat.setState(FlightSeatDocument.FlightState.CANCELLED);

            List<FlightSeatDocument> seats = new ArrayList<>();
            seats.add(seat);

            FlightDocument flight = new FlightDocument();
            flight.setId("FL123");
            flight.setAirline("Airline");
            flight.setDeparture("CityA");
            flight.setDestination("CityB");
            flight.setDepartureTime(Date.from(Instant.now().plus(2, ChronoUnit.HOURS)));
            flight.setArrivalTime(Date.from(Instant.now().plus(5, ChronoUnit.HOURS)));
            flight.setSeats(seats);

            List<FlightDocument> flights = new ArrayList<>();
            flights.add(flight);

            when(flightRepository.findFlightsByPassengerId("passenger123")).thenReturn(flights);

            List<FlightReservationResponse> result = getFlightsReservationHandler.getFlightsReservation();

            assertEquals(0, result.size());
        }
    }
}

