package unittest.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.entity.RoomReservationEntity;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.GetHotelByIdHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetHotelByIdHandlerQueryUnitTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private GetHotelByIdHandler getHotelByIdHandler;
    private Session adminSession;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
        adminSession = Session.newBuilder()
                .withId(UUID.randomUUID())
                .withEmail("admin@gmail.com")
                .withRoles("ADMIN")
                .build();
    }

    @Test
    void testGetHotelById_Successful() {
        SessionContextHolder.setSession(adminSession);

        UUID hotelId = UUID.randomUUID();
        HotelEntity mockHotel = new HotelEntity();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(mockHotel));
        Optional<HotelEntity> result = getHotelByIdHandler.getHotelById(hotelId);
        assertTrue(result.isPresent());
        assertEquals(mockHotel, result.get());
    }

    @Test
    void testGetHotelById_HotelNotFound() {
        SessionContextHolder.setSession(adminSession);

        UUID hotelId = UUID.randomUUID();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());
        Optional<HotelEntity> result = getHotelByIdHandler.getHotelById(hotelId);
        assertFalse(result.isPresent());
    }

    @Test
    void testGetHotelById_HotelIdIsNull() {
        SessionContextHolder.setSession(adminSession);

        UUID hotelId = null;
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getHotelByIdHandler.getHotelById(hotelId);
        });
        assertEquals("The hotel ID is null", exception.getMessage());
    }
}
