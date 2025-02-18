package unittest.commands;

import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.DeleteReservationHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteReservationHandlerCommandUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private DeleteReservationHandler deleteReservationHandler;

    private UUID reservationId;
    private Session adminSession;
    private Session userSession;

    @BeforeEach
    void setUp() {
        reservationId = UUID.randomUUID();

        adminSession = Session.newBuilder()
                .withId(UUID.randomUUID())
                .withEmail("adminSenior@gmail.com")
                .withRoles("ADMIN")
                .build();

        userSession = Session.newBuilder()
                .withId(UUID.randomUUID())
                .withEmail("userJr@gmail.com")
                .withRoles("USER")
                .build();
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testDeleteReservation_AsAdmin() {
        SessionContextHolder.setSession(adminSession);

        deleteReservationHandler.deleteReservation(reservationId);

        verify(reservationRepository, times(1)).deleteById(reservationId);
    }

    @Test
    void testDeleteReservation_AsUser() {
        SessionContextHolder.setSession(userSession);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            deleteReservationHandler.deleteReservation(reservationId);
        });

        assertEquals("The user does not have permissions", exception.getMessage());
        verify(reservationRepository, never()).deleteById(any(UUID.class));
    }


    @Test
    void testDeleteReservation_NoSession() {
        SessionContextHolder.clearSession();

        assertThrows(NullPointerException.class, () -> {
            deleteReservationHandler.deleteReservation(reservationId);
        });

        verify(reservationRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testDeleteReservation_NullReservationId() {
        SessionContextHolder.setSession(adminSession);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            deleteReservationHandler.deleteReservation(null);
        });

        assertEquals("The reservation ID is null", exception.getMessage());
    }

}