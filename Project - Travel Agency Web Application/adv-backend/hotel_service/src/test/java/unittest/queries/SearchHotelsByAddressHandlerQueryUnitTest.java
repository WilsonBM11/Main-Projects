package unittest.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.AddressHotelsRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.SearchHotelsByAddressHandler;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchHotelsByAddressHandlerQueryUnitTest {

    @Mock
    private AddressHotelsRepository addressHotelsRepository;

    @InjectMocks
    private SearchHotelsByAddressHandler searchHotelsByAddressHandler;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testSearchHotelsByAddress_AuthenticatedUser() {
        when(addressHotelsRepository.findByAddressContainingIgnoreCase(anyString()))
                .thenReturn(Collections.singletonList(new HotelEntity()));

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<HotelEntity> result = searchHotelsByAddressHandler.searchHotelsByAddress("Next to Coco´s Island");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(addressHotelsRepository, times(1)).findByAddressContainingIgnoreCase("Next to Coco´s Island");
    }

    @Test
    void testSearchHotelsByAddress_NullAddress() {
        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            searchHotelsByAddressHandler.searchHotelsByAddress(null);
        });

        assertTrue(exception.getMessage().contains("The address is null"));
    }

    @Test
    void testSearchHotelsByAddress_EmptyAddress() {
        when(addressHotelsRepository.findByAddressContainingIgnoreCase(anyString()))
                .thenReturn(Collections.emptyList());

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<HotelEntity> result = searchHotelsByAddressHandler.searchHotelsByAddress("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(addressHotelsRepository, times(1)).findByAddressContainingIgnoreCase("");
    }
}
