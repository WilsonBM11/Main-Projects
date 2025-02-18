package unittest.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.ReservationRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.RegisterReservationHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import dev.hotel_service.util.EmailUtility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jms.core.JmsTemplate;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterReservationHandlerCommandUnitTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JmsTemplate jmsTemplate;

    @Mock
    private EmailUtility emailUtility;

    @InjectMocks
    private RegisterReservationHandler registerReservationHandler;

    private UUID roomId;
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        SessionContextHolder.clearSession();
        roomId = UUID.randomUUID();
        Session session = new Session(UUID.randomUUID(), "userJr@gmail.com", null, true, null);
        SessionContextHolder.setSession(session);
    }

    @AfterEach
    void tearDown() throws Exception {
        SessionContextHolder.clearSession();
        closeable.close();
    }

    //
    @Test
    void testRegisterReservation_Successful() {
        // Arrange
        RegisterReservationHandler.Command command = new RegisterReservationHandler.Command("01/01/2025", "10/01/2025", "userJr@gmail.com", roomId);
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setAvailable(true);
        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setName("Hotel Test");
        roomEntity.setHotelId(hotelEntity);
        when(roomRepository.findAll()).thenReturn(List.of(roomEntity));
        when(emailUtility.reservationMessageToEmail(any(), any(), any())).thenReturn("Se realizó correctamente tu reserva!!!");

        // Act
        registerReservationHandler.registerReservation(command);

        // Verify
        verify(reservationRepository, times(1)).save(any(RoomReservationEntity.class));
        verify(roomRepository, times(1)).save(any(RoomEntity.class));
        verify(jmsTemplate, times(1)).convertAndSend(eq("message"), eq("Se realizó correctamente tu reserva!!!"));
        verify(jmsTemplate, times(1)).convertAndSend(eq("subject"), eq("Detalles de la reserva"));
        verify(jmsTemplate, times(1)).convertAndSend(eq("toUser"), eq("userJr@gmail.com"));
    }

    @Test
    void testRegisterReservation_RoomNotFound() {
        // Arrange
        RegisterReservationHandler.Command command = new RegisterReservationHandler.Command("01/01/2025", "10/01/2025", "userJr@gmail.com", roomId);
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerReservationHandler.registerReservation(command));

        // Verify
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testRegisterReservation_RoomUnavailable() {
        // Arrange
        RegisterReservationHandler.Command command = new RegisterReservationHandler.Command("01/01/2025", "10/01/2025", "userJr@gmail.com", roomId);
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setAvailable(false);
        when(roomRepository.findAll()).thenReturn(List.of(roomEntity));

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerReservationHandler.registerReservation(command));

        // Verify
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testRegisterReservation_InvalidStartDate() {
        // Arrange
        RegisterReservationHandler.Command command = new RegisterReservationHandler.Command("01/01/2020", "10/01/2025", "userJr@gmail.com", roomId);
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setAvailable(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerReservationHandler.registerReservation(command));

        // Verify
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testRegisterReservation_InvalidDateFormat() {
        // Arrange
        RegisterReservationHandler.Command command = new RegisterReservationHandler.Command("2025-01-01", "2025-01-10", "userJr@gmail.com", roomId);
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomId);
        roomEntity.setAvailable(true);

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerReservationHandler.registerReservation(command));

        // Verify
        verify(reservationRepository, never()).save(any(RoomReservationEntity.class));
        verify(roomRepository, never()).save(any(RoomEntity.class));
        verify(jmsTemplate, never()).convertAndSend(anyString(), anyString());
    }
}
