import { useEffect, useState } from "react";
import { useApiHandler } from "../../hooks/useApiHandler";
import { FlightDetails, FlightId } from "./types";
import { FlightDetailsRequest } from "../../models/flights.models"
import { getFlightDetails } from "../../services/flights.service";

const useDependencies = () =>{
    
    const [flightData, setFlightData] = useState<FlightDetails | null>(null);

    const {handleQuery} = useApiHandler();

    const handleFlightDetailsRequest = async (value: string) => {

        const id: FlightDetailsRequest = {
            id: value
        }

        const {isError,message,result} = await handleQuery(getFlightDetails, id);
        if (!isError) {
            console.log('Flight Details Obtained Successfully');
            const flightDetails: FlightDetails = {
                id: result.id,
                airline: result.airline,
                departure: result.departure,
                destination: result.destination,
                departureTime: result.departureTime,
                arrivalTime: result.arrivalTime,
                firstClassPrice: result.firstClassPrice,
                businessClassPrice: result.businessClassPrice,
                commercialClassPrice: result.commercialClassPrice,
                availableFirstClassSeats: result.availableFirstClassSeats,
                availableBusinessClassSeats: result.availableBusinessClassSeats,
                availableCommercialClassSeats: result.availableCommercialClassSeats,
            }
            console.log(flightDetails);
            setFlightData(flightDetails);
            return ;
        } else {
            console.log(message);
            return null;
        }
    }    
    
    return {flightData, handleFlightDetailsRequest}
}

export default useDependencies;