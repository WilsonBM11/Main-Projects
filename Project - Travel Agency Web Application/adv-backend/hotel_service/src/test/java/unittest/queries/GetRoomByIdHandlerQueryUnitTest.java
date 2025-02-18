package unittest.queries;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.RoomRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.GetRoomByIdHandler;
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
public class GetRoomByIdHandlerQueryUnitTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private GetRoomByIdHandler getRoomByIdHandler;

    private UUID roomId;
    private RoomEntity room;

    private Session userSession;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
        roomId = UUID.randomUUID();

        room = new RoomEntity();
        room.setRoomId(roomId);

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
    void testGetRoomById_Success() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        Optional<RoomEntity> result = getRoomByIdHandler.getRoomById(roomId);

        assertTrue(result.isPresent());
        assertTrue(result.get().getRoomId().equals(roomId));
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testGetRoomById_RoomNotFound() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        Optional<RoomEntity> result = getRoomByIdHandler.getRoomById(roomId);

        assertTrue(result.isEmpty());
        verify(roomRepository, times(1)).findById(roomId);
    }

    @Test
    void testGetRoomById_NullRoomId() {
        SessionContextHolder.setSession(userSession);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getRoomByIdHandler.getRoomById(null);
        });

        assertTrue(exception.getMessage().contains("The room ID is null"));
        verify(roomRepository, never()).findById(any(UUID.class));
    }
}
