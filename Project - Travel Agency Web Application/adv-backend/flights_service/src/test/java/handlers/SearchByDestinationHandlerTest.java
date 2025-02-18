package handlers;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.commands.SearchByAirlineHandler;
import dev.flights.handlers.commands.SearchByDestinationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SearchByDestinationHandlerTest {
    @Mock
    private FlightRepository repository;

    @InjectMocks
    private SearchByDestinationHandler searchByDestinationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test //Test case for a successful search
    void testSearchByDestination(){
        String id = UUID.randomUUID().toString();
        String destination = "Costa Rica";

        Calendar calendar = Calendar.getInstance();

        FlightDocument flight = new FlightDocument();
        flight.setId(id);
        flight.setAirline("avianca");
        flight.setDeparture("Colombia");
        flight.setDestination(destination);
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

        Mockito.when(repository.findAllByDestination(destination)).thenReturn(flights);

        SearchByDestinationHandler.Command command = new SearchByDestinationHandler.Command("Costa Rica");

        var result = searchByDestinationHandler.search(command);

        Assertions.assertEquals(result.get(0).destination(), destination);
        Assertions.assertEquals(result.get(0).firstClassPrice(), flight.getFirstClassPrice());
        Assertions.assertEquals(result.get(0).departure(), flight.getDeparture());
        Assertions.assertEquals(result.get(0).airline(), flight.getAirline());
        Assertions.assertEquals(result.get(0).id(), id);

        Mockito.verify(repository, Mockito.times(1)).findAllByDestination(destination);
    }

    //test of search with null destination
    @Test
    void testSearchByDestination_NullDestination() {
        // Arrange
        SearchByDestinationHandler.Command command = new SearchByDestinationHandler.Command(null);

        // Act & Assert
        InvalidInputException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InvalidInputException.class,
                () -> searchByDestinationHandler.search(command)
        );
        assertEquals("Invalid Field Destination is required", exception.getMessage());
    }

    //test of search with empty destination
    @Test
    void testSearchByDestination_EmptyDestination() {
        SearchByDestinationHandler.Command command = new SearchByDestinationHandler.Command("");

        InvalidInputException exception = org.junit.jupiter.api.Assertions.assertThrows(
                InvalidInputException.class,
                () -> searchByDestinationHandler.search(command)
        );
        assertEquals("Invalid Field Destination is required", exception.getMessage());
    }


}
