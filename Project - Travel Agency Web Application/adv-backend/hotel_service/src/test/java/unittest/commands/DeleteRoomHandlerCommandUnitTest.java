package unittest.commands;

import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.DeleteRoomHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteRoomHandlerCommandUnitTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private DeleteRoomHandler deleteRoomHandler;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
    }
    @Test
    void testDeleteRoom_AsAdmin() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        Session adminSession = new Session(UUID.randomUUID(), "adminSenior@gmail.com", Arrays.asList("ADMIN"), true, UUID.randomUUID());
        SessionContextHolder.setSession(adminSession);

        // Act
        deleteRoomHandler.deleteRoom(roomId);

        // Assert
        verify(roomRepository, times(1)).deleteById(roomId);
    }


    @Test
    void testDeleteRoom_NoSession() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        SessionContextHolder.clearSession();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> deleteRoomHandler.deleteRoom(roomId));

        // Verify
        verify(roomRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testDeleteRoom_AsUser() {
        // Arrange
        UUID roomId = UUID.randomUUID();
        Session userSession = new Session(UUID.randomUUID(), "userJr@gmail.com", Arrays.asList("USER"), true, UUID.randomUUID());
        SessionContextHolder.setSession(userSession);

        // Act & Assert
        assertThrows(BusinessException.class, () -> deleteRoomHandler.deleteRoom(roomId));

        // Verify
        verify(roomRepository, never()).deleteById(any(UUID.class));
    }


    @Test
    void testDeleteRoom_NullRoomId() {
        // Arrange
        Session adminSession = new Session(UUID.randomUUID(), "adminSenior@gmail.com", Arrays.asList("ADMIN"), true, UUID.randomUUID());
        SessionContextHolder.setSession(adminSession);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            deleteRoomHandler.deleteRoom(null);
        });

        // Verify
        assertEquals("The room ID is null", exception.getMessage());
    }
}
