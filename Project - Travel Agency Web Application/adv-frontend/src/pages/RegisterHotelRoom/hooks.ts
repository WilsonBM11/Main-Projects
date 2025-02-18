import { useNavigate } from "react-router-dom";
import { useApiHandler } from "../../hooks/useApiHandler";
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { registerRoom } from "../../models/hotels.models";
import { hotelsList } from "../../models/hotels.models";
import { registerRoomHotel } from "../../services/hotel.service";
import { getHotels } from "../../services/hotel.service";
import { registerRoomForm } from "./types";

const useDependencies = () =>{
    const { handleMutation, handleQuery } = useApiHandler();
    const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();
    const navigate = useNavigate();

    const handleGetRoom = async () => {
        const hotelList: hotelsList = { hotels: [] };
        const { result, isError, message } = await handleQuery(getHotels, hotelList);
    
        if (isError) {
            console.log("Error al devolver los hoteles: ", message);
            setErrorNotification(message);
            return { hotels: [] };  // Devuelve un objeto con la estructura esperada
        } else {
            console.log(result);
            return { hotels: result };  // Devuelve los resultados directamente
        }
    };
    
    const handleRegisterRoom = async (values: registerRoomForm) => {
        const room: registerRoom = {
            roomNumber: values.roomNumber,
            details: values.details,
            price: values.price,
            available: values.available,
            hotelId: values.hotelId
        };

        console.log(room);

        const { result, isError, message } = await handleMutation(registerRoomHotel, room);

        if (isError) {
            console.log("Error en el registro de la habitación: ", message);
            setErrorNotification(message);
        }else{
            console.log("Registro exitoso: ", result);
            setInfoNotificaiton("The room has been successfully registered in the system");
            navigate('/hotels')
        }
    };

    const handleCancel = () => {
        navigate('/');
    };

    return { handleRegisterRoom, handleCancel, handleGetRoom };
};

export default useDependencies;
