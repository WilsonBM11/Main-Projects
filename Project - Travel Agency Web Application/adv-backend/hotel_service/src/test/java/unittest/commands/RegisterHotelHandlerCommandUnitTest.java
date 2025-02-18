package unittest.commands;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.HotelRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.commands.RegisterHotelHandler;
import dev.hotel_service.session.Session;
import dev.hotel_service.session.SessionContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterHotelHandlerCommandUnitTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private RegisterHotelHandler registerHotelHandler;


    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testCreateHotel_AsAdmin() {
        // Arrange
        Session adminSession = new Session(null, null, Collections.singletonList("ADMIN"), true, null);
        SessionContextHolder.setSession(adminSession);

        RegisterHotelHandler.Command command = new RegisterHotelHandler.Command("Hotel Azuli", 123456789, "Azuli@hotmail.com", "next to Azuli Beach", Collections.singletonList("WiFi"));

        // Act
        registerHotelHandler.createHotel(command);

        // Assert
        verify(hotelRepository, times(1)).save(any(HotelEntity.class));
    }

    @Test
    void testCreateHotel_AsUser() {
        // Arrange
        Session userSession = new Session(null, null, Collections.singletonList("USER"), true, null);
        SessionContextHolder.setSession(userSession);

        RegisterHotelHandler.Command command = new RegisterHotelHandler.Command("Hotel Azuli", 123456789, "Azuli@hotmail.com", "next to Azuli Beach", Collections.singletonList("WiFi"));

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerHotelHandler.createHotel(command));

        // Verify
        verify(hotelRepository, never()).save(any(HotelEntity.class));
    }


    @Test
    void testCreateHotel_NoSession() {
        // Arrange
        SessionContextHolder.clearSession();
        RegisterHotelHandler.Command command = new RegisterHotelHandler.Command("Hotel Azuli", 123456789, "Azuli@hotmail.com", "next to Azuli Beach", Collections.singletonList("WiFi"));

        // Act & Assert
        assertThrows(NullPointerException.class, () -> registerHotelHandler.createHotel(command));

        // Verify
        verify(hotelRepository, never()).save(any(HotelEntity.class));
    }

    @Test
    void testCreateHotel_IncompleteData() {
        // Arrange
        Session adminSession = new Session(null, null, Collections.singletonList("ADMIN"), true, null);
        SessionContextHolder.setSession(adminSession);

        RegisterHotelHandler.Command command = new RegisterHotelHandler.Command(null, 123456789, "Azuli@hotmail.com", "next to Azuli Beach", Collections.singletonList("WiFi"));

        // Act & Assert
        assertThrows(BusinessException.class, () -> registerHotelHandler.createHotel(command));

        // Verify
        verify(hotelRepository, never()).save(any(HotelEntity.class));
    }


    @Test
    void testCreateHotel_NullCommand() {
        // Arrange
        Session adminSession = new Session(null, null, Collections.singletonList("ADMIN"), true, null);
        SessionContextHolder.setSession(adminSession);
        RegisterHotelHandler.Command command = new RegisterHotelHandler.Command(null,null,null,null,null);
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            registerHotelHandler.createHotel(command);
        });
        // Verify
        assertEquals("Must complete all hotel data", exception.getMessage());
    }
}