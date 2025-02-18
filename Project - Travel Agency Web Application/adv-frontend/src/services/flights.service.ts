
import { FlightDetailsRequest, FlightDetailsResponse, FlightEditRequest, FlightReservationRequest, FlightResponse, SearchAirline, SearchDestination,registerAirlinePayload,registerFlight } from "../models/flights.models";
import { doGet, doPost, doPut } from './http.service';


export const searchAirline = async (airline: SearchAirline): Promise<FlightResponse[]> => {
    const result = await doPost<SearchAirline, FlightResponse[]>(
        airline,
        '/api/private/flights/searchByAirline'
  );
    return result;
}


export const register = async (flight: registerFlight): Promise<string> => {
    const result = await doPost<registerFlight, string>(
        flight,
        '/api/private/flights/flight'
    );
    return result;
}

export const searchDestination = async (destination: SearchDestination): Promise<FlightResponse[]> => {
    const result = await doPost<SearchDestination, FlightResponse[]>(
        destination,
        '/api/private/flights/searchByDestination'
    );
    return result;
}

export const getFlightDetails = async (id: FlightDetailsRequest): Promise<FlightDetailsResponse> => {
    const result = await doPost<FlightDetailsRequest, FlightDetailsResponse>(
        id,
        '/api/private/flights/searchById'
    );
    return result;
}


export const userReservations = async (): Promise<FlightReservationResponse[]> => {
    const result = await doGet<FlightReservationResponse[]>(
        '/api/private/flights/GetFlightsReservation'
    );
    return result;
};

export const cancelReservation = async (seatId: CancelFlightRequest): Promise<string> => {
    const result = await doPost<CancelFlightRequest, string>(
        seatId,
        '/api/private/flights/cancelFlight'
    );
    return result;
};

export const reserveFlightSeat = async (id: FlightReservationRequest): Promise<string> => {
    const result = await doPost<FlightReservationRequest, string>(
        id,
        '/api/private/flights/reserveFlight'
    );
    return result;
}
export const editFlight = async (flight: FlightEditRequest): Promise<FlightEditRequest> => {
    const result = await doPut<FlightEditRequest, FlightEditRequest>(
        flight,
        '/api/private/flights/changeDetailsFlight'
    );
    return result;
}
export const registerAirline = async (flight: registerAirlinePayload): Promise<string> => {
    const result = await doPost<registerAirlinePayload, string>(
        flight,
        '/api/private/flights/registerAirline'
    );
    return result;
}
export const getAirlines = async (): Promise<string[]> => {
    const result = await doGet<string[]>(
        '/api/private/flights/getAirlines'
    );
    return result;
}
export const getDestinies = async (): Promise<string[]> => {
    const result = await doGet<string[]>(
        '/api/private/flights/getDestinies'
    );
    return result;
}

export const getHomeFlights = async (): Promise<FlightResponse[]> => {
    const result = await doGet<FlightResponse[]>('/api/private/flights/home');
    return result;
}
