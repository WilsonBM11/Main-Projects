import { SearchAirline, FlightResponse, SearchDestination } from './types';
import { useApiHandler } from '../../hooks/useApiHandler';
import { searchAirline, searchDestination, getAirlines, getDestinies } from '../../services/flights.service';
import { useEffect, useState } from 'react';
import { Option } from './types';
import { useNavigate } from 'react-router-dom';

const useDependencies = () => {

    const [optionsAirlines, setOptionsAirline] = useState<Option[]>([]);
    const [destinyOptions, setDestinyOptions] = useState<Option[]>([]);
    const navigate = useNavigate();

    const handleDetails = (flightId: string) => {
        navigate(`/flights/${flightId}`);
    };

    const handleReservation = (flightId: string) => {
        navigate(`/flights/reserve/${flightId}`);
    };

    const handleEdit = (flightId: string) => {
        navigate(`/flights/reserveEdit/${flightId}`);
    };

    const { handleQuery } = useApiHandler();
    
    const handleSearchAirline = async (values: SearchAirline) => {
        console.log('Received values of form: ', values);
        const airline: SearchAirline = {
            airline: values.airline
        };
        const { isError, message, result } = await handleQuery(searchAirline, airline);
        if (!isError) {
            console.log('Search successful');
            return result;
        } else {
            return null;
        }
    };

    const handleGetAirlines = async () => {
		let optionsAirlines: Option[] = [];
	  
		// Se define pero no se usa en la consulta, puede ser un array vacío
		let airlinesString: string[] = [];
	  
		const { message, isError, result } = await handleQuery(getAirlines, airlinesString);
		
		console.log(result);

		const resultAirlines = result as string[];
		
		if (!isError && result) {
            optionsAirlines = resultAirlines.map((airline:string, index:number) => ({
			label: `${airline}`,
			value: `${airline}`,
		  }));
		  setOptionsAirline(optionsAirlines);
		} else {
		  console.error(message);
		}
	};

    const handleGetDestinies = async () => {
		let destinyOptions: Option[] = [];
	  
		// Se define pero no se usa en la consulta, puede ser un array vacío
		let destinyString: string[] = [];
	  
		const { message, isError, result } = await handleQuery(getDestinies, destinyString);
		
		console.log(result);

		const resultDestiny = result as string[];
		
		if (!isError && result) {
            destinyOptions = resultDestiny.map((airline:string, index:number) => ({
			label: `${airline}`,
			value: `${airline}`,
		  }));
		  setDestinyOptions(destinyOptions);
		} else {
		  console.error(message);
		}
	};

    const handleSearchDestination = async (values: SearchDestination) => {
        const destination: SearchDestination = {
            destination: values.destination
        };
        const { isError, message, result } = await handleQuery(searchDestination, destination);
        if (!isError) {
            console.log('Search successful');
            return result;
        } else {
            return null;
        }
    };

    const listTransform = (list: string[]) => {
        let options = list.map((item) => {
            return {
                value: item,
                label: item,
            };
        });
        options.unshift({
            value: 'All',
            label: 'All',
        });
        return options;
    };
    return { handleSearchAirline, handleSearchDestination, handleDetails, handleReservation, handleGetAirlines, handleGetDestinies, optionsAirlines, destinyOptions, listTransform, handleEdit };
};
export default useDependencies;
