export type FlightDetails = {
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

export type FlightId = {
	id: string;
};
