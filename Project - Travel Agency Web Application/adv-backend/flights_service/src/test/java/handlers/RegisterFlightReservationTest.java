package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.commands.RegisterFlightReservationHandler;
import dev.flights.handlers.types.PassengerCompanion;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.jms.core.JmsTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegisterFlightReservationTest {

    @Mock
    private FlightRepository repository;

    @Mock
    private JmsTemplate jmsTemplate;

    @InjectMocks
    private RegisterFlightReservationHandler registerFlightReservationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterFlightReservation() {
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String flightId = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();

            FlightDocument flight = new FlightDocument();
            flight.setId(flightId);
            flight.setAirline("avianca");
            flight.setDeparture("Colombia");
            flight.setDestination("Costa Rica");
            flight.setFirstClassSeats(10);
            flight.setFirstClassPrice(200);
            flight.setBusinessClassSeats(10);
            flight.setBusinessClassPrice(150);
            flight.setCommercialClassSeats(10);
            flight.setCommercialClassPrice(150);
            flight.setAvailableCommercialClassSeats(5);
            flight.setSeats(new ArrayList<>());

            calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
            Date date = calendar.getTime();
            flight.setDepartureTime(date);

            calendar.set(2024, Calendar.JANUARY, 1, 2, 0, 0);
            date = calendar.getTime();
            flight.setArrivalTime(date);

            Mockito.when(repository.findById(flightId)).thenReturn(Optional.of(flight));

            String passengerName = "Juanito alimaña";
            String dateOfBirth = "15/08/2003";
            String passengerEmail = "juanitoalimaña@gmail.com";
            String passengerTelephone = "70701111";

            RegisterFlightReservationHandler.Command command = new RegisterFlightReservationHandler.Command(flightId, FlightSeatDocument.FlightClass.COMMERCIAL, passengerName, dateOfBirth, passengerEmail, passengerTelephone, Optional.empty());

            var result = registerFlightReservationHandler.handle(command);

            Assertions.assertEquals(result, "OK");

            verify(repository).save(flight);
            verify(jmsTemplate, times(1)).convertAndSend(eq("message"), anyString());
            verify(jmsTemplate, times(1)).convertAndSend(eq("subject"), eq("Detalles de la reserva"));
            verify(jmsTemplate, times(1)).convertAndSend(eq("toUser"), eq(passengerEmail));
        }
    }

    @Test
    void testRegisterFlightReservation_UnavailableSeats() {
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String flightId = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();

            FlightDocument flight = new FlightDocument();
            flight.setId(flightId);
            flight.setAirline("avianca");
            flight.setDeparture("Colombia");
            flight.setDestination("Costa Rica");
            flight.setFirstClassSeats(10);
            flight.setFirstClassPrice(200);
            flight.setBusinessClassSeats(10);
            flight.setBusinessClassPrice(150);
            flight.setCommercialClassSeats(10);
            flight.setCommercialClassPrice(150);
            flight.setAvailableCommercialClassSeats(0);
            flight.setSeats(new ArrayList<>());

            calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
            Date date = calendar.getTime();
            flight.setDepartureTime(date);

            calendar.set(2024, Calendar.JANUARY, 1, 2, 0, 0);
            date = calendar.getTime();
            flight.setArrivalTime(date);

            Mockito.when(repository.findById(flightId)).thenReturn(Optional.of(flight));

            String passengerName = "Cristofer";
            String dateOfBirth = "15/08/2003";
            String passengerEmail = "cristofer@gmail.com";
            String passengerTelephone = "11223344";

            RegisterFlightReservationHandler.Command command = new RegisterFlightReservationHandler.Command(flightId, FlightSeatDocument.FlightClass.COMMERCIAL, passengerName, dateOfBirth, passengerEmail, passengerTelephone, Optional.empty());

            assertThrows(BusinessException.class, () -> registerFlightReservationHandler.handle(command));
        }
    }

    @Test
    void testRegisterFlightReservation_NullCommand() {
        assertThrows(NullPointerException.class, () -> registerFlightReservationHandler.handle(null));
    }

    @Test
    void testRegisterFlightReservation_InvalidInput() {
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String flightId = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();

            FlightDocument flight = new FlightDocument();
            flight.setId(flightId);
            flight.setAirline("avianca");
            flight.setDeparture("Colombia");
            flight.setDestination("Costa Rica");
            flight.setFirstClassSeats(10);
            flight.setFirstClassPrice(200);
            flight.setBusinessClassSeats(10);
            flight.setBusinessClassPrice(150);
            flight.setCommercialClassSeats(10);
            flight.setCommercialClassPrice(150);
            flight.setAvailableCommercialClassSeats(2);
            flight.setSeats(new ArrayList<>());

            calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
            Date date = calendar.getTime();
            flight.setDepartureTime(date);

            calendar.set(2024, Calendar.JANUARY, 1, 2, 0, 0);
            date = calendar.getTime();
            flight.setArrivalTime(date);

            Mockito.when(repository.findById(flightId)).thenReturn(Optional.of(flight));

            String passengerName = "";
            String dateOfBirth = "15/08/2003";
            String passengerEmail = "cristofer@gmail.com";
            String passengerTelephone = "11223344";

            RegisterFlightReservationHandler.Command command = new RegisterFlightReservationHandler.Command(flightId, FlightSeatDocument.FlightClass.COMMERCIAL, passengerName, dateOfBirth, passengerEmail, passengerTelephone, Optional.empty());

            assertThrows(BusinessException.class, () -> registerFlightReservationHandler.handle(command));
        }
    }

    @Test
    void testRegisterFlightReservation_UnavailableSeatsCompanions() {
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String flightId = UUID.randomUUID().toString();
            Calendar calendar = Calendar.getInstance();

            FlightDocument flight = new FlightDocument();
            flight.setId(flightId);
            flight.setAirline("avianca");
            flight.setDeparture("Colombia");
            flight.setDestination("Costa Rica");
            flight.setFirstClassSeats(10);
            flight.setFirstClassPrice(200);
            flight.setBusinessClassSeats(10);
            flight.setBusinessClassPrice(150);
            flight.setCommercialClassSeats(10);
            flight.setCommercialClassPrice(150);
            flight.setAvailableCommercialClassSeats(2);
            flight.setSeats(new ArrayList<>());

            calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
            Date date = calendar.getTime();
            flight.setDepartureTime(date);

            calendar.set(2024, Calendar.JANUARY, 1, 2, 0, 0);
            date = calendar.getTime();
            flight.setArrivalTime(date);

            Mockito.when(repository.findById(flightId)).thenReturn(Optional.of(flight));

            String passengerName = "Cristofer";
            String dateOfBirth = "2003-08-15 00:00:00";
            String passengerEmail = "cristofer@gmail.com";
            String passengerTelephone = "11223344";

            List<PassengerCompanion> companions = new ArrayList<>();
            companions.add(new PassengerCompanion("Ignacio", "2003-11-9 00:00:00"));
            companions.add(new PassengerCompanion("Wilson", "2003-08-15 00:00:00"));

            RegisterFlightReservationHandler.Command command = new RegisterFlightReservationHandler.Command(flightId, FlightSeatDocument.FlightClass.COMMERCIAL, passengerName, dateOfBirth, passengerEmail, passengerTelephone, Optional.of(companions));

            assertThrows(BusinessException.class, () -> registerFlightReservationHandler.handle(command));
        }
    }
}
