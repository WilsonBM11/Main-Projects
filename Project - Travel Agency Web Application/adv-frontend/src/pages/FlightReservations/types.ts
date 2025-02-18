type FlightReservationResponse = {
    id : string,
    airline: string,
    departure: string,
    destination: string,
    departureTime: string,
    arrivalTime: string,
    seats: FlightSeatResponse[]
};

type FlightSeatResponse = {
    seatId: string,
    passengerId: string,
    passengerName: string,
    seatClass: string,
    seatPrice: number,
    seatState: string,
};

type CancelFlightRequest = {
    seatId: string
};
