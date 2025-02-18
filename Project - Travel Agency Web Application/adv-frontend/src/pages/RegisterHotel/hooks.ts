import { useNavigate } from 'react-router-dom';
import { useApiHandler } from '../../hooks/useApiHandler';
import { registerHotel } from '../../models/hotels.models';
import { register } from '../../services/hotel.service';
import { registerHotelForm } from "./types";
import useAlerts from '../Alerts/Alerts';
import useNotificationHandler from '../../hooks/useNotificationHandler';

const useDependencies = () => {
    const { handleMutation } = useApiHandler();
    const navigate = useNavigate();
    const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();
    const { successAlert, errorAlert, alertClosed, alert } = useAlerts();

    const handleRegisterHotel = async (values: registerHotelForm) => {
        const hotel: registerHotel = {
            name: values.name,
            phoneNumber: values.phoneNumber,
            email: values.email,
            address: values.address,
            facilities: values.facilities,
        };
        console.log(hotel);
        const { result, isError, message } = await handleMutation(register, hotel);

        if (isError) {
            console.error("Error en el registro del hotel: ", message);
            //errorAlert(message);
            setErrorNotification(message);
        } else {
            console.log("Registro exitoso:", result);
            //successAlert(message);
            //window.location.reload();
            setInfoNotificaiton('The hotel has been successfully registered in the system');
            navigate('/hotels');
        }
    };

    const handleCancel = () => {
        navigate('/');
    };

    return { handleRegisterHotel, handleCancel, alert };
};

export default useDependencies;
