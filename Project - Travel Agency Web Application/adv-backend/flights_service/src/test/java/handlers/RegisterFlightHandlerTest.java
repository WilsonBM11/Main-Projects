package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.commands.RegisterFlightHandler;

import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RegisterFlightHandlerTest {

    @Mock
    private FlightRepository repository;

    @InjectMocks
    private RegisterFlightHandler registerFlightHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    //Test successful fligth registration
    @Test
    void testRegisterFlight() {
        List<String> roles = List.of("ADMIN");

        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterFlightHandler.Command command = new RegisterFlightHandler.Command(
                    "Airline",
                    "Departure",
                    "Destination",
                    "2022-03-03 00:00:00",
                    "2022-03-03 02:00:00",
                    10,
                    10,
                    10,
                    100.0,
                    200.0,
                    300.0
            );

            registerFlightHandler.register(command);

            verify(repository, times(1)).save(any(FlightDocument.class));
        }
    }

    //Test flight registration with null command
    @Test
    void testRegisterFlight_NullCommand() {
        assertThrows(NullPointerException.class, () -> registerFlightHandler.register(null));

        verifyNoInteractions(repository);
    }

    //Test flight registration with invalid date
    @Test
    void testRegisterFlight_InvalidDate() {
        List<String> roles = List.of("ADMIN");

        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterFlightHandler.Command command = new RegisterFlightHandler.Command(
                    "Airline",
                    "Departure",
                    "Destination",
                    "InvalidDate",
                    "InvalidDate",
                    10,
                    10,
                    10,
                    100.0,
                    200.0,
                    300.0
            );

            assertThrows(BusinessException.class, () -> registerFlightHandler.register(command));

            verifyNoInteractions(repository);
        }
    }

    @Test
    void testRegisterFlight_NullInput() {

        List<String> roles = List.of("ADMIN");

        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);


            RegisterFlightHandler.Command command = new RegisterFlightHandler.Command(
                    "Airline",
                    "",
                    "Destination",
                    "2022-03-03 00:00:00",
                    "2022-03-03 02:00:00",
                    10,
                    10,
                    10,
                    100.0,
                    200.0,
                    300.0
            );

            assertThrows(BusinessException.class, () -> registerFlightHandler.register(command));

            verifyNoInteractions(repository);
        }
    }

    @Test
    void testRegisterFlight_InvalidInput() {

        List<String> roles = List.of("ADMIN");

        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterFlightHandler.Command command = new RegisterFlightHandler.Command(
                    "Airline",
                    "Departure",
                    "Destination",
                    "2022-03-03 00:00:00",
                    "2022-03-03 02:00:00",
                    10,
                    -5,
                    10,
                    100.0,
                    200.0,
                    300.0
            );

            assertThrows(BusinessException.class, () -> registerFlightHandler.register(command));

            verifyNoInteractions(repository);
        }
    }

    //Test flight registration without user permission
    @Test
    void testRegisterFlight_InvalidUserRole() {

        List<String> roles = List.of("ACCOUNT_MANAGER");

        var sessionMock = mock(Session.class);
        Mockito.when(sessionMock.roles()).
                thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterFlightHandler.Command command = new RegisterFlightHandler.Command(
                    "Airline",
                    "Departure",
                    "Destination",
                    "2022-03-03 00:00:00",
                    "2022-03-03 02:00:00",
                    10,
                    15,
                    10,
                    100.0,
                    200.0,
                    300.0
            );

            assertThrows(BusinessException.class, () -> registerFlightHandler.register(command));

            verifyNoInteractions(repository);
        }
    }

}








