import { useNavigate } from "react-router-dom";
import { useApiHandler } from "../../hooks/useApiHandler";
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { HotelResponse, RoomResponse, reserveRoom } from "../../models/hotels.models";
import { getHotel, getRooms, reservationRoom } from "../../services/hotel.service";
import { roomReservationRequest } from "./types";

import moment from 'moment'
import { formatDateDDMMYYYY } from "../../utils/common.utils";


const useDependencies = () => {

	const { handleQuery , handleMutation} = useApiHandler();
    const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();
	
	const navigate = useNavigate();

    const rules = {
        userEmail: [{ required: true, message: 'Please enter your email' },
                { type: 'email', message: 'This is not a valid email' }],
        startDate: [{ required: true, message: 'Please select a start date' }],
        endDate: [{ required: true, message: 'Please select a end date' }]
    };

    const handleDetails = (hotelId: any) => {
        navigate(`/hotelDetail/${hotelId}`); // Navega a la página de detalles del hotel con el hotelId correspondiente
    };

	const handleGetAccepted = async (hotelId: string): Promise<HotelResponse | null> => {
        
        const { isError, message } = await handleQuery(getHotel, hotelId);
        if (isError) {
            setErrorNotification(message);
            return null;
        } else {
            const result = await getHotel(hotelId);
            console.log(result);
            return result;
        }
    };

    const handleGetRooms = async (hotelId: string): Promise<RoomResponse[] | null> => {
        const { isError, message } = await handleQuery(getRooms, hotelId);
        if (isError) {
            console.log(message);
            setErrorNotification(message);
            return null;
        } else {
            const result = await getRooms(hotelId);
            console.log(result);
            console.log(message);
            return result;
        }
    };
	const handleHotels = () => {
		 navigate('/hotels');
	};

    const handleReservations = async (values : reserveRoom) =>{
        const formattedStartDate = formatDateDDMMYYYY(values.startDate);
        const formattedEndDate = formatDateDDMMYYYY(values.endDate);
        const room : reserveRoom = {
            startDate: formattedStartDate,
            endDate : formattedEndDate,
            userEmail: values.userEmail,
            roomId : values.roomId
        }
        console.log(values.endDate)
        console.log(room)
        
        const { result, isError, message } = await handleMutation(reservationRoom,room);

        if (isError) {
            console.error("Error on the room reservation: ", message);
            setErrorNotification(message);
        } else {
            console.log("Reserva exitosa:", result);
            setInfoNotificaiton("The reservation has been successfully registered in the system");
            navigate('/hotels');
        }
        
    };

	return { handleGetAccepted, handleHotels, handleDetails, handleGetRooms, handleReservations, rules};
};


export default useDependencies
