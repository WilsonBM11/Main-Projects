package unittest.queries;

import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.GetReservationByIdHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetReservationByIdHandlerQueryUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private GetReservationByIdHandler getReservationByIdHandler;

    private UUID reservationId;
    private RoomReservationEntity reservation;

    private Session userSession;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();

        reservationId = UUID.randomUUID();

        reservation = new RoomReservationEntity();
        reservation.setReservationId(reservationId);

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
    void testGetReservationById_Success() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        Optional<RoomReservationEntity> result = getReservationByIdHandler.getReservationById(reservationId);

        assertTrue(result.isPresent());
        assertTrue(result.get().getReservationId().equals(reservationId));
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testGetReservationById_ReservationNotFound() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        Optional<RoomReservationEntity> result = getReservationByIdHandler.getReservationById(reservationId);

        assertTrue(result.isEmpty());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    //Revisar
    @Test
    void testGetReservationById_NullReservationId() {
        SessionContextHolder.setSession(userSession);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getReservationByIdHandler.getReservationById(null);
        });

        assertTrue(exception.getMessage().contains("The reservation ID is null"));
        verify(reservationRepository, never()).findById(any(UUID.class));
    }
}
