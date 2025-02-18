import { useNavigate } from "react-router-dom";
import { useApiHandler } from "../../hooks/useApiHandler"
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { PendingReservationsResponse, ReservationsRoomsResponse } from "../../models/hotels.models";
import { cancelRoomReserve, getReservationsRooms } from "../../services/hotel.service";

 const useDependencies = () => {
    const { handleQuery, handleMutation } = useApiHandler();
    const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();
    const navigate = useNavigate();

    const handleCancelReservation =  async (reservationId: string) => {

        const { result, isError, message } = await handleMutation(cancelRoomReserve, reservationId);

        if (isError) {
            console.error("Error en el registro del hotel: ", message);
            setErrorNotification(message);
        } else {
            console.log("Cancelacion exitosa:", result);
            setInfoNotificaiton("The reservation has been successfully canceled");
        }
    } 

    const handleGetAccepted = async () => {
        const reservationsList : PendingReservationsResponse = {
            reservations : []
        
        };

        const { isError, message, result } = await handleQuery(getReservationsRooms, reservationsList);

        if(isError) {
            setErrorNotification(message);
            return null;
        }else{
            console.log(message);
            return result;
        }
        
    };

    return {handleCancelReservation, handleGetAccepted};

 };

 export default useDependencies;

