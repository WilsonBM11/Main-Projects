package handlers;


import dev.flights.database.documents.AirlineDocument;
import dev.flights.database.repositories.AirlinesRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.handlers.commands.RegisterAirlineHandler;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterAirlineHandlerTest {

    @Mock
    private AirlinesRepository airlinesRepository;

    @InjectMocks
    private RegisterAirlineHandler registerAirlineHandler;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

   @Test
    void testRegisterAirline_Successful() {
        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        when(sessionMock.roles()).thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterAirlineHandler.Command command = new RegisterAirlineHandler.Command("Airline Name");

            registerAirlineHandler.register(command);

            verify(airlinesRepository, times(1)).save(any(AirlineDocument.class));
        }
    }

    @Test
    void testRegisterAirline_NullCommand() {
        assertThrows(NullPointerException.class, () -> registerAirlineHandler.register(null));

        verifyNoInteractions(airlinesRepository);
    }

    @Test
    void testRegisterAirline_EmptyAirlineName() {
        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        when(sessionMock.roles()).thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterAirlineHandler.Command command = new RegisterAirlineHandler.Command("");

            BusinessException exception = assertThrows(BusinessException.class, () -> registerAirlineHandler.register(command));
            assertEquals("Must complete all flight data", exception.getMessage());

            verifyNoInteractions(airlinesRepository);
        }
    }

    @Test
    void testRegisterAirline_UserNotAdmin() {
        List<String> roles = List.of("USER");
        var sessionMock = mock(Session.class);
        when(sessionMock.roles()).thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterAirlineHandler.Command command = new RegisterAirlineHandler.Command("Airline Name");

            BusinessException exception = assertThrows(BusinessException.class, () -> registerAirlineHandler.register(command));
            assertEquals("The user cannot add flights", exception.getMessage());

            verifyNoInteractions(airlinesRepository);
        }
    }

    @Test
    void testRegisterAirline_GenerateUUID() {
        List<String> roles = List.of("ADMIN");
        var sessionMock = mock(Session.class);
        when(sessionMock.roles()).thenReturn(roles);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = mockStatic(SessionContextHolder.class)) {
            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            RegisterAirlineHandler.Command command = new RegisterAirlineHandler.Command("Airline Name");

            registerAirlineHandler.register(command);

            ArgumentCaptor<AirlineDocument> captor = ArgumentCaptor.forClass(AirlineDocument.class);
            verify(airlinesRepository).save(captor.capture());

            AirlineDocument savedDocument = captor.getValue();
            assertNotNull(savedDocument.getId());
            assertEquals("Airline Name", savedDocument.getAirline());
        }
    }
}
