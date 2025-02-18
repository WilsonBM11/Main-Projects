package unittest.commands;


import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.DeleteHotelHandler;
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
public class DeleteHotelHandlerCommandUnitTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private DeleteHotelHandler deleteHotelHandler;

    private UUID hotelId;
    private Session adminSession;
    private Session userSession;

    @BeforeEach
    void setUp() {
        hotelId = UUID.randomUUID();

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
    void testDeleteHotel_AsAdmin() {
        SessionContextHolder.setSession(adminSession);

        deleteHotelHandler.deleteHotel(hotelId);

        verify(hotelRepository, times(1)).deleteById(hotelId);
    }

    @Test
    void testDeleteHotel_AsUser() {
        SessionContextHolder.setSession(userSession);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            deleteHotelHandler.deleteHotel(hotelId);
        });

        assertEquals("The user does not have permissions", exception.getMessage());
        verify(hotelRepository, never()).deleteById(any(UUID.class));
    }


    @Test
    void testDeleteHotel_NoSession() {
        SessionContextHolder.clearSession();

        assertThrows(NullPointerException.class, () -> {
            deleteHotelHandler.deleteHotel(hotelId);
        });

        verify(hotelRepository, never()).deleteById(any(UUID.class));
    }

    @Test
    void testDeleteHotel_NullHotelId() {
        SessionContextHolder.setSession(adminSession);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            deleteHotelHandler.deleteHotel(null);
        });
        assertEquals("The hotel ID is null", exception.getMessage());

    }
}