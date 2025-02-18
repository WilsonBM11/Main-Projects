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

export type SearchForm = {
    airline: string;
    destination: string;
};

export type SearchAirline = {
    airline: string;
};

export type SearchDestination = {
    destination: string;
};


export type SearchPrice = {
    price: string;
};

export interface Option {
    value: string;
    label: string;
};


