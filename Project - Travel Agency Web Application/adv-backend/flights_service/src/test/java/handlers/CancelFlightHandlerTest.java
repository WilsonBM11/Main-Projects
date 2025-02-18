package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.documents.FlightSeatDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.commands.CancelFlightHandler;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CancelFlightHandlerTest {

    @Mock
    FlightRepository repository;

    @Mock
    JmsTemplate jmsTemplate;

    @InjectMocks
    CancelFlightHandler cancelFlightHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Test successful flight cancellation
    @Test
    void testCancelFlight_Success() {
        //Mock session
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        // Mock session context holder
        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            // Setup mock flight and seat
            String seatId = "seat123";
            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId(seatId);
            seat.setPassengerId(passengerId);
            seat.setPrice(100.0);
            seat.setPassengerEmail("user@example.com");
            FlightDocument flight = new FlightDocument();
            flight.setSeats(new ArrayList<>());
            flight.getSeats().add(seat);
            flight.setDepartureTime(new Date(System.currentTimeMillis() + 1000000000));

            when(repository.findFlightBySeatId(seatId)).thenReturn(Optional.of(flight));


            CancelFlightHandler.Command command = new CancelFlightHandler.Command(seatId);
            cancelFlightHandler.cancelFlight(command);


            assert seat.getState() == FlightSeatDocument.FlightState.CANCELLED;
            assert seat.isAvailable();
            verify(repository, times(1)).save(any(FlightDocument.class));
            verify(jmsTemplate, times(1)).convertAndSend(eq("message"), anyString());
            verify(jmsTemplate, times(1)).convertAndSend(eq("subject"), eq("Cancelacion de vuelo exitosa"));
            verify(jmsTemplate, times(1)).convertAndSend(eq("toUser"), eq(seat.getPassengerEmail()));
        }
    }

    //Test seat not found
    @Test
    void testCancelFlight_SeatNotFound() {
        //Mock session
        String passengerId = "passenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(passengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            when(repository.findFlightBySeatId(anyString())).thenReturn(Optional.empty());


            CancelFlightHandler.Command command = new CancelFlightHandler.Command("seat123");
            assertThrows(BusinessException.class, () -> cancelFlightHandler.cancelFlight(command));

            verify(repository, never()).save(any(FlightDocument.class));
        }

    }

    // Test seat not reserved by the current user
    @Test
    void testCancelFlight_NotReservedByCurrentUser() {
        //Mock session
        String currentPassengerId = "currentPassenger123";
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.getId()).thenReturn(currentPassengerId);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);


            String seatId = "seat123";
            FlightSeatDocument seat = new FlightSeatDocument();
            seat.setSeatId(seatId);
            seat.setPassengerId("anotherPassenger123");
            FlightDocument flight = new FlightDocument();
            flight.setSeats(new ArrayList<>());
            flight.getSeats().add(seat);

            when(repository.findFlightBySeatId(seatId)).thenReturn(Optional.of(flight));

            // Execute and expect exception
            CancelFlightHandler.Command command = new CancelFlightHandler.Command(seatId);
            assertThrows(BusinessException.class, () -> cancelFlightHandler.cancelFlight(command));

            verify(repository, never()).save(any(FlightDocument.class));
        }
    }
}
