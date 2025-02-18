type PassengerCompanion = {
    name: string;
    dateOfBirth: string; 
};

export type FlightReservation = {
    flightId: string;
    flightClass: 'FIRST' | 'BUSINESS' | 'COMMERCIAL';
    passengerName: string;
    dateOfBirth: string;
    passengerEmail: string;
    passengerTelephone: string;
    companions?: PassengerCompanion[];
};