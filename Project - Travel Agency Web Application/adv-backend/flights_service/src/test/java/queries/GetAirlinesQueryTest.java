package queries;

import dev.flights.database.documents.AirlineDocument;
import dev.flights.database.repositories.AirlinesRepository;
import dev.flights.handlers.queries.GetAirlinesQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAirlinesQueryTest {

    @Mock
    private AirlinesRepository airlinesRepository;

    @InjectMocks
    private GetAirlinesQuery getAirlinesQuery;

    @Test
    public void testHandleReturnsCorrectAirlines() {
        AirlineDocument airline1 = new AirlineDocument();
        airline1.setAirline("Airline A");
        AirlineDocument airline2 = new AirlineDocument();
        airline2.setAirline("Airline B");
        AirlineDocument airline3 = new AirlineDocument();
        airline3.setAirline("Airline C");

        List<AirlineDocument> airlines = Arrays.asList(airline1, airline2, airline3);
        when(airlinesRepository.findAll()).thenReturn(airlines);
        List<String> result = getAirlinesQuery.handle();

        assertEquals(3, result.size());
        assertEquals(List.of("Airline A", "Airline B", "Airline C"), result);
    }

    @Test
    public void testHandleReturnsEmptyListWhenNoAirlines() {
        when(airlinesRepository.findAll()).thenReturn(List.of());

        List<String> result = getAirlinesQuery.handle();

        assertEquals(0, result.size());
    }

    @Test
    public void testHandleWithNullAirlines() {
        AirlineDocument airline1 = new AirlineDocument();
        airline1.setAirline("Airline A");
        AirlineDocument airline2 = new AirlineDocument();
        airline2.setAirline(null);
        AirlineDocument airline3 = new AirlineDocument();
        airline3.setAirline("Airline C");

        List<AirlineDocument> airlines = Arrays.asList(airline1, airline2, airline3);
        when(airlinesRepository.findAll()).thenReturn(airlines);

        List<String> result = getAirlinesQuery.handle();

        // Null values should be ignored or handled appropriately
        assertEquals(2, result.size());
        assertEquals(List.of("Airline A", "Airline C"), result);
    }

    @Test
    public void testHandleWithDuplicateAirlines() {
        AirlineDocument airline1 = new AirlineDocument();
        airline1.setAirline("Airline A");
        AirlineDocument airline2 = new AirlineDocument();
        airline2.setAirline("Airline A");
        AirlineDocument airline3 = new AirlineDocument();
        airline3.setAirline("Airline C");

        List<AirlineDocument> airlines = Arrays.asList(airline1, airline2, airline3);
        when(airlinesRepository.findAll()).thenReturn(airlines);

        List<String> result = getAirlinesQuery.handle();

        // Duplicates should be preserved
        assertEquals(2, result.size());
        assertEquals(List.of("Airline A", "Airline C"), result);
    }

    @Test
    public void testHandleWithEmptyAirlineNames() {
        AirlineDocument airline1 = new AirlineDocument();
        airline1.setAirline("");
        AirlineDocument airline2 = new AirlineDocument();
        airline2.setAirline(" ");
        AirlineDocument airline3 = new AirlineDocument();
        airline3.setAirline("Airline C");

        List<AirlineDocument> airlines = Arrays.asList(airline1, airline2, airline3);
        when(airlinesRepository.findAll()).thenReturn(airlines);

        List<String> result = getAirlinesQuery.handle();

        assertEquals(3, result.size());
        assertEquals(List.of("", " ", "Airline C"), result);
    }

    @Test
    public void testHandleWithSpecialCharactersInNames() {
        AirlineDocument airline1 = new AirlineDocument();
        airline1.setAirline("Airline @#!");
        AirlineDocument airline2 = new AirlineDocument();
        airline2.setAirline("Airline &*()_");
        AirlineDocument airline3 = new AirlineDocument();
        airline3.setAirline("Airline C");

        List<AirlineDocument> airlines = Arrays.asList(airline1, airline2, airline3);
        when(airlinesRepository.findAll()).thenReturn(airlines);

        List<String> result = getAirlinesQuery.handle();

        assertEquals(3, result.size());
        assertEquals(List.of("Airline @#!", "Airline &*()_", "Airline C"), result);
    }


}

