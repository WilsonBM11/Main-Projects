
export type FlightResponse = {
    id: string;
    airline: string;
    departure: string;
    destination: string;
    departureTime: string;
    arrivalTime: string;
    firstClassPrice: number;
    businessClassPrice: number;
    commercialClassPrice: number;
};

export type SearchAirline = {
	airline: string;
};

export type SearchDestination = {
	destination: string;
};

export type registerAirlinePayload = {
    airline:string;
}

export type registerFlight = {
    airplane:string
    departure:string
    destination:string
    airline:string
    departureTime:string
    arrivalTime:string
    commercialClassPrice:number
    commercialClassSeats:number
    businessClassPrice:number
    businessClassSeats:number
    firstClassPrice:number
    firstClassSeats:number
}

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


export type FlightDetailsRequest = {
	id: string;
};

export type FlightDetailsResponse = {
    id: string;
    airline: string;
    departure: string;
    destination: string;
    departureTime: string;
    arrivalTime: string;
    firstClassPrice: number;
    businessClassPrice: number;
    commercialClassPrice: number;
    availableFirstClassSeats: number;
    availableBusinessClassSeats: number;
    availableCommercialClassSeats: number;
};

type PassengerCompanion = {
    name: string;
    dateOfBirth: string; 
};

export type FlightReservationRequest = {
    flightId: string;
    flightClass: 'FIRST' | 'BUSINESS' | 'COMMERCIAL';
    passengerName: string;
    dateOfBirth: string;
    passengerEmail: string;
    passengerTelephone: string;
    companions?: PassengerCompanion[];
};

export type FlightEditRequest = {
    flightId:string,
    firstClass: FlightAvailability | null,
    businessClass: FlightAvailability | null,
    commercialClass: FlightAvailability | null
}
export type FlightAvailability = {
    newPrice:number | null,
    availableSeats:number | null
}
