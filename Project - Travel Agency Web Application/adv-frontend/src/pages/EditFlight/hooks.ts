import { useEffect, useState } from "react";
import { useApiHandler } from "../../hooks/useApiHandler";
import { FlightAvailability, FlightDetails, FlightEditForm, FlightEditRequest, FlightId } from "./types";
import { FlightDetailsRequest } from "../../models/flights.models"
import { editFlight, getFlightDetails } from "../../services/flights.service";
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { ErrorResponse } from "react-router-dom";

const useDependencies = () =>{
    
    

    const {handleQuery, handleMutation} = useApiHandler();
    const {setInfoNotificaiton, setErrorNotification} = useNotificationHandler();

    const handleFlightDetailsRequest = async (value: string) => {

        const id: FlightDetailsRequest = {
            id: value
        }

        console.log(value);

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
            
            return flightDetails;
        } else {
            console.log(message);
            return null;
        }
    }   
    
    

    const handleFlightEdit = async (resultForm: FlightEditForm, flightBefore:FlightDetails) => {
        console.log("form recibido 1");
        console.log(resultForm)

        let firstClass:FlightAvailability ={
            availableSeats:null,
            newPrice:null
        }
        let secondClass:FlightAvailability ={
            availableSeats:null,
            newPrice:null
        }
        let thirdClass:FlightAvailability ={
            availableSeats:null,
            newPrice:null
        }
        let flightEdit:FlightEditRequest = {
            flightId: resultForm.id,
            businessClass:null,
            commercialClass:null,
            firstClass:null
        }
        
        console.log("form recibido 2");
        console.log(flightBefore);
        
        if (resultForm.availableBusinessClassSeats != flightBefore?.availableBusinessClassSeats) {
            secondClass.availableSeats = resultForm.availableBusinessClassSeats;
            flightEdit.businessClass = secondClass
        }
        if (resultForm.availableFirstClassSeats != flightBefore?.availableFirstClassSeats) {
            firstClass.availableSeats = resultForm.availableFirstClassSeats;
            flightEdit.firstClass = firstClass
        }
        if (resultForm.availableCommercialClassSeats != flightBefore?.availableCommercialClassSeats) {
            thirdClass.availableSeats = resultForm.availableCommercialClassSeats;
            flightEdit.commercialClass = thirdClass   
        }
        if (resultForm.firstClassPrice != flightBefore?.firstClassPrice) {
            firstClass.newPrice =  resultForm.firstClassPrice;
            flightEdit.firstClass = firstClass
        }
        if (resultForm.businessClassPrice != flightBefore?.businessClassPrice) {
            secondClass.newPrice = resultForm.businessClassPrice;
            flightEdit.businessClass = secondClass
        }
        if (resultForm.commercialClassPrice != flightBefore?.commercialClassPrice) {
            thirdClass.newPrice = resultForm.commercialClassPrice;
            flightEdit.commercialClass = thirdClass 
        }

        console.log("form recibido 3");
        console.log(flightEdit);

        const {message, isError} = await handleMutation(editFlight, flightEdit);
        if(isError){
            setErrorNotification(message);
        }else{
            setInfoNotificaiton(message);
        }

            

    }
    
    return { handleFlightDetailsRequest, handleFlightEdit}
}

export default useDependencies;