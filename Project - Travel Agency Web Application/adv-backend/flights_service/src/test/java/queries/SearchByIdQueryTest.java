package queries;

import dev.flights.database.documents.FlightDocument;
import dev.flights.database.repositories.FlightRepository;
import dev.flights.exceptions.BusinessException;
import dev.flights.exceptions.InvalidInputException;
import dev.flights.handlers.queries.SearchByIdQuery;
import dev.flights.session.Session;
import dev.flights.session.SessionContextHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

public class SearchByIdQueryTest {

    @Mock
    private FlightRepository repository;

    @InjectMocks
    private SearchByIdQuery searchByIdQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test//Test case for a successful search
    void testSearchById(){

        var sessionMock = mock(Session.class);

        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

           sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String id = UUID.randomUUID().toString();

            Calendar calendar = Calendar.getInstance();

            FlightDocument flight = new FlightDocument();
            flight.setId(id);
            flight.setAirline("avianca");
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


            Mockito.when(repository.findById(id)).thenReturn(Optional.of(flight));

            SearchByIdQuery.Command command = new SearchByIdQuery.Command(id);

            var result = searchByIdQuery.query(command);

            Assertions.assertEquals(result.id(), id);
            Assertions.assertEquals(result.firstClassPrice(), flight.getFirstClassPrice());
            Assertions.assertEquals(result.departure(), flight.getDeparture());
            Assertions.assertEquals(result.airline(), flight.getAirline());


            Mockito.verify(repository, Mockito.times(1)).findById(id);

        }

    }

    @Test
    void testSearchById_FlightNotExists(){
        var sessionMock = mock(Session.class);

        Mockito.when(sessionMock.authenticated()).thenReturn(true);

        try (MockedStatic<SessionContextHolder> sessionContextHolder = Mockito.mockStatic(SessionContextHolder.class)) {

            sessionContextHolder.when(SessionContextHolder::getSession).thenReturn(sessionMock);

            String id = UUID.randomUUID().toString();

            FlightDocument flight = new FlightDocument();

            Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

            SearchByIdQuery.Command command = new SearchByIdQuery.Command(id);

            assertThrows(BusinessException.class, () -> searchByIdQuery.query(command));
        }
    }

    @Test
    void testSearchById_NullCommand() {
        assertThrows(NullPointerException.class, () -> searchByIdQuery.query(null));
    }

    @Test
    void testSearchById_EmptyId() {
        SearchByIdQuery.Command command = new SearchByIdQuery.Command("");

        assertThrows(InvalidInputException.class, () -> searchByIdQuery.query(command));
    }
}
