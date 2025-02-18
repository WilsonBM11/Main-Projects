import { validationSchema } from '../../utils/forms.util';
import { RegisterFlightForm } from './types';
import { useNavigate } from 'react-router-dom';
import { useApiHandler } from '../../hooks/useApiHandler';
import useNotificationHandler from '../../hooks/useNotificationHandler';
import { Option } from './types';
import { registerFlight } from '../../models/flights.models';
import { getAirlines, register } from '../../services/flights.service';
import { useState } from 'react';

const useDependencies = () => {

	const [options, setOptions] = useState<Option[]>([]);

	const { handleMutation,  handleQuery } = useApiHandler();
	const { setErrorNotification, setInfoNotificaiton } =
		useNotificationHandler();

	const navigate = useNavigate();

	const initialValues = {
		username: '',
		password: '',
	};
	const rules = {
		username: [validationSchema.requiredFieldRule],
		password: [validationSchema.requiredFieldRule],
	};

	// TODO #lc: Esperaría que estos datos sean temporales y se obtengan del BE
	const handleGetAirlines = async () => {
		let options: Option[] = [];
	  
		// Se define pero no se usa en la consulta, puede ser un array vacío
		let airlinesString: string[] = [];
	  
		const { message, isError, result } = await handleQuery(getAirlines, airlinesString);
		
		console.log(result);

		const resultAirlines = result as string[];
		
		if (!isError && result) {
		  options = resultAirlines.map((airline:string, index:number) => ({
			label: `${airline}`,
			value: `${airline}`,
		  }));
		  setOptions(options);
		} else {
		  console.error(message);
		}
	};
	

	

	const handleRegisterFlight = async (values: RegisterFlightForm) => {
		const flight: registerFlight = {
			airline: values.airline[0],
			airplane: values.airplane[0],
			departure: values.departure,
			destination: values.destination,
			arrivalTime: values.dd_datetime[1].format("YYYY-MM-DD HH:mm:ss"),
            departureTime: values.dd_datetime[0].format("YYYY-MM-DD HH:mm:ss"),
			firstClassPrice: values.firstClassPrice,
			firstClassSeats: values.firstClassSeats,
			businessClassPrice: values.secondClassPrice,
			businessClassSeats: values.secondClassSeats,
			commercialClassPrice: values.thirdClassPrice,
			commercialClassSeats: values.thirdClassSeats,
		};

		console.log(flight);

		const { result, isError, message } = await handleMutation(register, flight);

		if (isError) {
			console.log(message);
			setErrorNotification(message);
		} else {
			setInfoNotificaiton(
				'The flight has been successfully registered in the system'
			);
			navigate('/');
		}
	};
	const handleCancel = () => {
		navigate('/');
	};

	return {
		initialValues,
		rules,
		handleRegisterFlight,
		handleCancel,
		options,
		handleGetAirlines
	};
};

export default useDependencies;
