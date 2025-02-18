export type FlightEditForm = {
    id: string;
    firstClassPrice: number | null;
    businessClassPrice: number | null;
    commercialClassPrice: number | null;
    availableFirstClassSeats: number | null;
    availableBusinessClassSeats: number | null;
    availableCommercialClassSeats: number | null;
}
export type FlightDetails =  {
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
}
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

export type flightEditProps = {
    id:string
}

export type FlightId = {
	id: string;
};
