export type registerHotel = {
    name:string
    phoneNumber:number
    email:string
    address:string
    facilities:string[]
}

export type detailsHotel = {
    hotelId: string;
    name: string;
    phoneNumber: string;
    email: string;
    address: string;
    facilities: string[];
}


export type hotelsList = {
    hotels:detailsHotel[]
}


export type HotelResponse = {
    hotelId: string;
    name: string;
    phoneNumber: string;
    email: string;
    address: string;
    facilities: string[];
};

export type PendingHotelDetail = {
    hotels : HotelResponse[];
};

export type RoomResponse = {
    roomId: string;
    roomNumber: number;
    details: string;
    price: number;
    available: boolean;
    hotelId: HotelResponse
};

export type registerRoom = {
    roomNumber:number
    details:string
    price:number
    available:boolean
    hotelId:string
}

export type reserveRoom = {
    startDate: string
    endDate: string
    userEmail: string
    roomId: string
}

export type ReservationsRoomsResponse = {
    reservationId:string
    startDate: string
    endDate: string
    canceled: boolean
    userEmail: string
    roomId: RoomResponse
}

export type PendingReservationsResponse = {
    reservations :ReservationsRoomsResponse[]
}