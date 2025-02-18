package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.commands.ChangeDetailsFlightHandler;
import dev.flights.handlers.types.FlightPriceAvailability;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ChangeDetailsFlightTest {

    @Mock
    private FlightRepository repository;

    @InjectMocks
    private ChangeDetailsFlightHandler handler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testChangeFlightDetailsSuccessfully() {

        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            ChangeDetailsFlightHandler.Command command = new ChangeDetailsFlightHandler.Command(
                    "flightId123",
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(500.0), OptionalInt.of(10))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(300.0), OptionalInt.of(20))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(100.0), OptionalInt.of(30)))
            );

            FlightDocument flightDocument = new FlightDocument();
            Mockito.when(repository.findById("flightId123")).thenReturn(Optional.of(flightDocument));

            String result = handler.handle(command);

            assertEquals("OK", result);
            verify(repository).save(flightDocument);
        }
    }

    @Test
    public void testInvalidDataFields() {

        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            ChangeDetailsFlightHandler.Command command = new ChangeDetailsFlightHandler.Command(
                    "flightId123",
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(500.0), OptionalInt.of(-1))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(-300.0), OptionalInt.of(20))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(0.0), OptionalInt.of(30)))
            );

            BusinessException exception = assertThrows(BusinessException.class, () -> handler.handle(command));
            assertEquals("Invalid available seats value for First Class", exception.getMessage());
        }
    }

    @Test
    public void testFlightDoesNotExist() {

        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            ChangeDetailsFlightHandler.Command command = new ChangeDetailsFlightHandler.Command(
                    "flightId123",
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(500.0), OptionalInt.of(10))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(300.0), OptionalInt.of(20))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(100.0), OptionalInt.of(30)))
            );

            when(repository.findById("flightId123")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(BusinessException.class, () -> handler.handle(command));
            assertEquals("This flight does not exist", exception.getMessage());
        }
    }

    @Test
    public void testUserWithoutAdminPermission() {

        List<String> roles = List.of("ACCOUNT_MANAGER");
        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            ChangeDetailsFlightHandler.Command command = new ChangeDetailsFlightHandler.Command(
                    "flightId123",
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(500.0), OptionalInt.of(10))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(300.0), OptionalInt.of(20))),
                    Optional.of(new FlightPriceAvailability(OptionalDouble.of(100.0), OptionalInt.of(30)))
            );

            BusinessException exception = assertThrows(BusinessException.class, () -> handler.handle(command));
            assertEquals("The user cannot update flights prices or seats availability", exception.getMessage());
        }
    }
}
