import { useApiHandler } from "../../hooks/useApiHandler";
import { FlightResponse } from "../../models/flights.models";
import { getHomeFlights } from "../../services/flights.service";
import { getHomeHotels } from "../../services/hotel.service";
import { randomFlights, randomHotels } from "./types";

const userDependencies = () => {

    const { handleQuery } = useApiHandler();

    const handleRandomFlights = async () => {
        const flightList: randomFlights = {
            flights: []
        };
        const { isError, message, result } = await handleQuery(getHomeFlights, flightList);
        if (isError) {
            console.error(message);
            return null;
        } else {
            console.log(result);
            return result;
        }
    };

    const handleRandomHotels = async () => {
        const flightList: randomHotels = {
            hotels: []
        };
        const { isError, message, result } = await handleQuery(getHomeHotels, flightList);
        if (isError) {
            console.error(message);
            return null;
        } else {
            console.log(result);
            return result;
        }
    };

    return { handleRandomFlights, handleRandomHotels };

};

export default userDependencies;

