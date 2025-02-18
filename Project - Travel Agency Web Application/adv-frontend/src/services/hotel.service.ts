import { doGet, doPost } from './http.service';
import { registerHotel, detailsHotel, HotelResponse, ReservationsRoomsResponse, reserveRoom } from '../models/hotels.models';
import { registerRoom } from '../models/hotels.models';
import { RoomResponse } from '../models/hotels.models';

export const register = async (hotel: registerHotel): Promise<string> => {
    const result = await doPost<registerHotel, string>(hotel, '/api/private/hotels/registerHotel');
    return result;
};

export const getHotels = async (): Promise<detailsHotel[]> => {
    const result = await doGet<detailsHotel[]>('/api/private/hotels/getHotels');
    return result;
}

export const getHotel = async (hotelId: string): Promise<HotelResponse> => {
    try {
        const response = await doGet<HotelResponse>(`/api/private/hotels/getHotelById/${hotelId}`);
        return response; // Devuelve los datos de respuesta
    } catch (error) {
        console.error('Error fetching hotel:', error);
        throw error; // Lanza el error para manejarlo en el lugar donde se llame a esta función
    }
};

export const registerRoomHotel = async (hotel: registerRoom): Promise<string> => {
    const result = await doPost<registerRoom, string>(hotel, '/api/private/hotels/registerRoom');
    return result;
}; 

export const getRooms = async (hotelId: string): Promise<RoomResponse[]> => {
    try {
        const response = await doGet<RoomResponse[]>(`/api/private/hotels/getRooms/${hotelId}`);
        return response;
    }catch (error){
        console.error('Error fetching room hotel: ', error);
        throw error;
    }
}

export const reservationRoom = async (reserve: reserveRoom): Promise<string> => {
    const result = await doPost<reserveRoom, string>(reserve,'/api/private/hotels/registerReservation');
    return result;
};

export const getReservationsRooms = async (): Promise<ReservationsRoomsResponse[]> => {
    try {
        const response = await doGet<ReservationsRoomsResponse[]>('/api/private/hotels/getReservationsByUser');
        return response;
    }catch (error){
        console.error('Error fetching reservation room hotel: ', error);
        throw error;
    }
}

export const cancelRoomReserve = async (reservationId: string): Promise<string> =>{
    const result = await doPost<string,string>(reservationId,`/api/private/hotels/cancelReservation/${reservationId}`);
    return result;
}

export const searchHotelName = async (name: string): Promise<detailsHotel[]> => {
    try {
        const result = await doGet<detailsHotel[]>(`/api/private/hotels/searchByName/${name}`);
        return result;
    }catch (error){
        console.error('Error fetching reservation room hotel: ', error);
        throw error;
    }
};

export const searchHotelByLocation = async (address: string): Promise<detailsHotel[]> => {
    try {
        const result = await doGet<detailsHotel[]>(`/api/private/hotels/searchByAddress/${address}`);
        return result;
    }catch (error){
        console.error('Error fetching reservation room hotel: ', error);
        throw error;
    }
    
};


export const getHomeHotels = async (): Promise<detailsHotel[]> => {
    const result = await doGet<detailsHotel[]>('/api/private/hotels/home');
    return result;
}
