import { validationSchema } from '../../utils/forms.util';
import { useNavigate } from 'react-router-dom';
import { useApiHandler } from '../../hooks/useApiHandler';
import useNotificationHandler from '../../hooks/useNotificationHandler';
import { PendingHotelDetail, hotelsList } from '../../models/hotels.models';
import { getHotels, searchHotelName, searchHotelByLocation } from '../../services/hotel.service';

const useDependencies = () => {
    const { handleQuery } = useApiHandler();
    const { setErrorNotification } = useNotificationHandler();
    const navigate = useNavigate();

    const handleDetails = (hotelId: string) => {
        navigate(`/hotelDetail/${hotelId}`); // Navega a la página de detalles del hotel con el hotelId correspondiente
    };

    const handleGetAccepted = async () => {
        const hotelList: PendingHotelDetail = {
            hotels: []
        };
        const { isError, message, result } = await handleQuery(getHotels, hotelList);
        if (isError) {
            setErrorNotification(message);
            return null;
        } else {
            console.log(result);
            return result;
        }
    };

    const handleSearchHotelByName = async (name: string) => {
        try {
            const result = await searchHotelName(name);
            return result;
        } catch (error) {
            console.error(error);
            setErrorNotification('Error fetching hotels by name');
            return [];
        }
    };

    const handleSearchHotelByLocation = async (location: string) => {
        try {
            const result = await searchHotelByLocation(location);
            return result;
        } catch (error) {
            console.error(error);
            setErrorNotification('Error fetching hotels by location');
            return [];
        }
    };

    const handleHotels = () => {
        navigate('/hotels');
    };

    return { handleGetAccepted, handleHotels, handleDetails, handleSearchHotelByName, handleSearchHotelByLocation };
};

export default useDependencies;
