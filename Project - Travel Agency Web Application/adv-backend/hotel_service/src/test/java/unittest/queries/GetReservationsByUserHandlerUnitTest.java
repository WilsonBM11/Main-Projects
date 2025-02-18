package unittest.queries;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationsByUserRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.GetReservationsByUserHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetReservationsByUserHandlerUnitTest {

    @Mock
    private ReservationsByUserRepository reservationsByUserRepository;

    @InjectMocks
    private GetReservationsByUserHandler getReservationsByUserHandler;

    private UUID userId;
    private RoomReservationEntity reservation;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();

        userId = UUID.randomUUID();

        reservation = new RoomReservationEntity();
        reservation.setUserId(userId);
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testGetReservationsByUser_Success() {
        when(reservationsByUserRepository.findReservationsByUserId(userId)).thenReturn(Collections.singletonList(reservation));

        Session session = new Session(userId, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<RoomReservationEntity> result = getReservationsByUserHandler.getReservationsByUser();

        assertTrue(result.size() == 1);
        assertTrue(result.get(0).getUserId().equals(userId));
        verify(reservationsByUserRepository, times(1)).findReservationsByUserId(userId);
    }

    @Test
    void testGetReservationsByUser_UserNotFound() {
        when(reservationsByUserRepository.findReservationsByUserId(userId)).thenReturn(Collections.emptyList());

        Session session = new Session(userId, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<RoomReservationEntity> result = getReservationsByUserHandler.getReservationsByUser();

        assertTrue(result.isEmpty());
        verify(reservationsByUserRepository, times(1)).findReservationsByUserId(userId);
    }

    @Test
    void testGetReservationsByUser_NullUserId() {
        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getReservationsByUserHandler.getReservationsByUser();
        });

        assertTrue(exception.getMessage().contains("User ID cannot be null or empty."));
        verify(reservationsByUserRepository, never()).findReservationsByUserId(any(UUID.class));
    }

}
