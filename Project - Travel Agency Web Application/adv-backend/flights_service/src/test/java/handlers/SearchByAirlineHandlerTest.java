package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.commands.SearchByAirlineHandler;
import dev.flights.handlers.types.SearchFlightResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class SearchByAirlineHandlerTest {
    @Mock
    private FlightRepository repository;

    @InjectMocks
    private SearchByAirlineHandler searchByAirlineHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test //Test case for a successful search
    void testSearchByAirline(){
        String id = UUID.randomUUID().toString();
        String airline = "avianca";

        Calendar calendar = Calendar.getInstance();

        FlightDocument flight = new FlightDocument();
        flight.setId(id);
        flight.setAirline(airline);
        flight.setDeparture("Colombia");
        flight.setDestination("Costa Rica");
        flight.setFirstClassSeats(10);
        flight.setFirstClassPrice(200);
        flight.setBusinessClassSeats(10);
        flight.setBusinessClassPrice(150);
        flight.setCommercialClassSeats(10);
        flight.setCommercialClassPrice(150);

        calendar.set(2024, Calendar.JANUARY, 1, 0, 0, 0);
        Date date = calendar.getTime();
        flight.setDepartureTime(date);

        calendar.set(2024, Calendar.JANUARY, 1, 2, 0, 0);
        date = calendar.getTime();
        flight.setArrivalTime(date);

        List<FlightDocument> flights = new ArrayList<>();
        flights.add(flight);

        Mockito.when(repository.findAllByAirline(airline)).thenReturn(flights);

        SearchByAirlineHandler.Command command = new SearchByAirlineHandler.Command("avianca");

        var result = searchByAirlineHandler.search(command);

        Assertions.assertEquals(result.get(0).airline(), airline);
        Assertions.assertEquals(result.get(0).firstClassPrice(), flight.getFirstClassPrice());
        Assertions.assertEquals(result.get(0).departure(), flight.getDeparture());
        Assertions.assertEquals(result.get(0).destination(), flight.getDestination());
        Assertions.assertEquals(result.get(0).id(), id);

        Mockito.verify(repository, Mockito.times(1)).findAllByAirline(airline);
    }

    @Test
    void testSearchByAirline_NullCommand() {
        assertThrows(NullPointerException.class, () -> searchByAirlineHandler.search(null));
    }
    //test case for empty airline
    @Test
    void testSearchByAirline_EmptyAirline() {
        SearchByAirlineHandler.Command command = new SearchByAirlineHandler.Command("");

        assertThrows(InvalidInputException.class, () -> searchByAirlineHandler.search(command));
    }
}
