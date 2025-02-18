package unittest.queries;

import dev.hotel_service.database.entity.RoomEntity;
import dev.hotel_service.database.repositories.AvailableRoomsRepository;
import dev.hotel_service.handler.queries.AvailableRoomsHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvailableRoomsHandlerQueryUnitTest {

    @Mock
    private AvailableRoomsRepository availableRoomsRepository;

    @InjectMocks
    private AvailableRoomsHandler availableRoomsHandler;


    @Test
    void testGetAvailableRoomsByHotel_Successful() {

        SessionContextHolder.clearSession();
        UUID hotelId = UUID.randomUUID();
        Session session = new Session(UUID.randomUUID(), "adminSenior@gmail.com", List.of("ADMIN"), true, null);
        SessionContextHolder.setSession(session);

        List<RoomEntity> mockRooms = new ArrayList<>();
        mockRooms.add(new RoomEntity());
        when(availableRoomsRepository.findByAvailableTrueAndHotelId_HotelId(hotelId)).thenReturn(mockRooms);


        List<RoomEntity> result = availableRoomsHandler.getAvailableRoomsByHotel(hotelId);


        assertEquals(mockRooms.size(), result.size());
    }

    @Test
    void testGetAvailableRoomsByHotel_UserNotAdmin() {

        SessionContextHolder.clearSession();
        UUID hotelId = UUID.randomUUID();
        Session session = new Session(UUID.randomUUID(), "userJr@gmail.com", List.of("USER"), true, null);
        SessionContextHolder.setSession(session);


        List<RoomEntity> result = availableRoomsHandler.getAvailableRoomsByHotel(hotelId);


        assertEquals(0, result.size());
    }
}
