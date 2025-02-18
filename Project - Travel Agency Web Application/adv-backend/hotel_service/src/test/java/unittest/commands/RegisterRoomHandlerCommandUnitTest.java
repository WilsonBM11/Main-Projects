package unittest.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.AvailableRoomsRepository;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.RegisterRoomHandler;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterRoomHandlerCommandUnitTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AvailableRoomsRepository availableRoomsRepository;

    @InjectMocks
    private RegisterRoomHandler registerRoomHandler;

    private UUID hotelId;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
        hotelId = UUID.randomUUID();
        Session session = new Session(UUID.randomUUID(), "adminSenior@gmail.com", List.of("ADMIN"), true, null);
        SessionContextHolder.setSession(session);
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testRegisterRoom_Successful() {
        // Arrange
        RegisterRoomHandler.Command command = new RegisterRoomHandler.Command(101, "Black and blue color", 15000.0f, hotelId);
        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setHotelId(hotelId);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotelEntity));
        when(availableRoomsRepository.findByAvailableTrueAndHotelId_HotelId(hotelId)).thenReturn(Collections.emptyList());

        // Act
        registerRoomHandler.registerRoom(command);

        // Verify
        verify(roomRepository, times(1)).save(any(RoomEntity.class));
    }

    @Test
    void testRegisterRoom_UserNotAdmin() {
        // Arrange
        SessionContextHolder.clearSession(); // Limpiar la sesión antes de establecerla nuevamente
        Session session = new Session(UUID.randomUUID(), "userJr@gmail.com", List.of("USER"), true, null);
        SessionContextHolder.setSession(session);

        RegisterRoomHandler.Command command = new RegisterRoomHandler.Command(101, "Black and blue color", 15000.0f, hotelId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerRoomHandler.registerRoom(command));

        // Verify
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }
    @Test
    void testRegisterRoom_HotelNotFound() {
        // Arrange
        RegisterRoomHandler.Command command = new RegisterRoomHandler.Command(101, "Black and blue color", 15000.0f, hotelId);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerRoomHandler.registerRoom(command));

        // Verify
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    void testRegisterRoom_RoomNumberExists() {
        // Arrange
        RegisterRoomHandler.Command command = new RegisterRoomHandler.Command(101, "Black and blue color", 15000.0f, hotelId);
        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setHotelId(hotelId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerRoomHandler.registerRoom(command));

        // Verify
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }

    @Test
    void testRegisterRoom_NegativePrice() {
        // Arrange
        RegisterRoomHandler.Command command = new RegisterRoomHandler.Command(101, "Black and blue color", -15000.0f, hotelId);
        HotelEntity hotelEntity = new HotelEntity();
        hotelEntity.setHotelId(hotelId);

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerRoomHandler.registerRoom(command));

        // Verify
        verify(roomRepository, never()).save(any(RoomEntity.class));
    }
}
