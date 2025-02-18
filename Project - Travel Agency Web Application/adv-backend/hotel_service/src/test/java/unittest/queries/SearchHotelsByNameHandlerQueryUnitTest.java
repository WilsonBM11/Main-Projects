package unittest.queries;

import dev.hotel_service.database.entity.HotelEntity;
import dev.hotel_service.database.repositories.NameHotelsRepository;
import dev.hotel_service.exceptions.BusinessException;
import dev.hotel_service.handler.queries.SearchHotelsByNameHandler;
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
public class SearchHotelsByNameHandlerQueryUnitTest {

    @Mock
    private NameHotelsRepository nameHotelsRepository;

    @InjectMocks
    private SearchHotelsByNameHandler searchHotelsByNameHandler;

    @BeforeEach
    void setUp() {
        SessionContextHolder.clearSession();
    }

    @AfterEach
    void tearDown() {
        SessionContextHolder.clearSession();
    }

    @Test
    void testSearchHotelsByName_AuthenticatedUser() {
        when(nameHotelsRepository.findByNameContainingIgnoreCase(anyString()))
                .thenReturn(Collections.singletonList(new HotelEntity()));

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<HotelEntity> result = searchHotelsByNameHandler.searchHotelsByName("Hilton");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(nameHotelsRepository, times(1)).findByNameContainingIgnoreCase("Hilton");
    }

    @Test
    void testSearchHotelsByName_NullName() {
        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            searchHotelsByNameHandler.searchHotelsByName(null);
        });

        assertTrue(exception.getMessage().contains("The name hotel is null"));
    }

    @Test
    void testSearchHotelsByName_EmptyName() {
        when(nameHotelsRepository.findByNameContainingIgnoreCase(anyString()))
                .thenReturn(Collections.emptyList());

        Session session = new Session(null, null, null, true, null);
        SessionContextHolder.setSession(session);

        List<HotelEntity> result = searchHotelsByNameHandler.searchHotelsByName("");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(nameHotelsRepository, times(1)).findByNameContainingIgnoreCase("");
    }
}
