
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

export type randomFlights = {
    flights: FlightResponse[]
};

export type HotelResponse = {
    hotelId: string;
    name: string;
    phoneNumber: string;
    email: string;
    address: string;
    facilities: string[];
};

export type randomHotels = {
    hotels: HotelResponse[]
};