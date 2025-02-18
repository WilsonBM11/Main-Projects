import { useApiHandler } from "../../hooks/useApiHandler"
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { FlightReservationRequest } from "../../models/flights.models";
import { reserveFlightSeat } from "../../services/flights.service";
import { FlightReservation } from "./types";


const useDependencies = () => {

    const {handleMutation} = useApiHandler();
    const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();

    const rules = {
        flightClass: [{ required: true, message: 'Select a flight seat class' }],
        passengerName: [{ required: true, message: 'Insert your name' }],
        dateOfBirth: [{ required: true, message: 'Insert your date of birth' }],
        passengerEmail: [{ required: true, message: 'Insert your email' },
                         { type: 'email', message: 'This is not a valid email' },],
        passengerTelephone: [{ required: true, message: 'Insert your contact telephone' }],
        companionName: [{ required: true, message: 'Insert your companion name' }],
        companionDateOfBirth: [{ required: true, message: 'Insert your companion date of birth' }],
    }

    const handleSubmit = async (values: FlightReservation) => {

        const flightReservationRequest: FlightReservationRequest = {
            flightId: values.flightId,
            flightClass: values.flightClass,
            passengerName: values.passengerName,
            dateOfBirth: values.dateOfBirth,
            passengerEmail: values.passengerEmail,
            passengerTelephone: values.passengerTelephone,
            companions: values.companions
        }

        const {isError,message} = await handleMutation(reserveFlightSeat, flightReservationRequest);
        
        if(!isError){
            console.log(message);
            setInfoNotificaiton("You have reserved your flight seats successfully")
        }else{
            console.log(message);
            setErrorNotification(message);
        }  
    };

    return {handleSubmit, rules}

}

export default useDependencies
