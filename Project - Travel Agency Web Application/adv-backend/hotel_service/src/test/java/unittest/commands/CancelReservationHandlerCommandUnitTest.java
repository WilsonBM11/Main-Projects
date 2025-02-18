package unittest.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.CancelReservationHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import dev.hotel_service.util.EmailUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CancelReservationHandlerCommandUnitTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private EmailUtility emailUtility;

    private CancelReservationHandler cancelReservationHandler;

    private UUID reservationId;
    private RoomReservationEntity activeReservation;
    private RoomReservationEntity canceledReservation;
    private RoomEntity room;

    @BeforeEach
    void setUp() {
        reservationId = UUID.randomUUID();

        room = new RoomEntity();
        room.setRoomId(UUID.randomUUID());
        room.setAvailable(false);

        activeReservation = new RoomReservationEntity();
        activeReservation.setReservationId(reservationId);
        activeReservation.setCanceled(false);
        activeReservation.setRoomId(room);

        canceledReservation = new RoomReservationEntity();
        canceledReservation.setReservationId(reservationId);
        canceledReservation.setCanceled(true);
        canceledReservation.setRoomId(room);


        cancelReservationHandler = new CancelReservationHandler(jmsTemplate);
        cancelReservationHandler.reservationRepository = reservationRepository;
        cancelReservationHandler.roomRepository = roomRepository;
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    //
    @Test
    void testCancelReservation_Success() {

        RoomEntity mockRoom = new RoomEntity();
        mockRoom.setRoomId(room.getRoomId());
        mockRoom.setAvailable(false);
        mockRoom.setHotelId(new HotelEntity());
        when(roomRepository.findAll()).thenReturn(List.of(mockRoom));

        when(reservationRepository.findAll()).thenReturn(List.of(activeReservation));

        cancelReservationHandler.cancelReservation(reservationId);

        verify(reservationRepository, times(1)).save(any(RoomReservationEntity.class));
        verify(roomRepository, times(1)).save(any(RoomEntity.class));

        assertEquals(true, activeReservation.getCanceled());
        assertEquals(true, room.getAvailable());
    }

    @Test
    void testCancelReservation_ReservationNotFound() {
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cancelReservationHandler.cancelReservation(reservationId);
        });

        assertEquals("Reservation not found", exception.getMessage());
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    void testCancelReservation_ReservationAlreadyCanceled() {
        when(reservationRepository.findAll()).thenReturn(List.of(canceledReservation));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cancelReservationHandler.cancelReservation(reservationId);
        });

        assertEquals("The reservation is already canceled", exception.getMessage());
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    void testCancelReservation_NullReservationId() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            cancelReservationHandler.cancelReservation(null);
        });

        assertEquals("The reservation ID is null", exception.getMessage());
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }
}
