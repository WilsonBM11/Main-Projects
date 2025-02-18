import { validationSchema } from '../../utils/forms.util';
import { useNavigate } from 'react-router-dom';
import { useApiHandler } from '../../hooks/useApiHandler';
import useNotificationHandler from '../../hooks/useNotificationHandler';
import { registerFlight } from '../../models/flights.models';
import { registerAirline } from '../../services/flights.service';
import { RegisterAirline } from './types';

const useDependencies = () => {
	const { handleMutation } = useApiHandler();
	const { setErrorNotification, setInfoNotificaiton } =
		useNotificationHandler();

	const navigate = useNavigate();

	const initialValues = {
		airline: ''
	};
	const rules = {
		airline: [validationSchema.requiredFieldRule]
	};

	const handleRegisterAirline = async (values: RegisterAirline) => {
		const body: RegisterAirline = {
			airline: values.airline
			
		};

		console.log(body);

		const { result, isError, message } = await handleMutation(registerAirline, body);

		if (isError) {
			console.log(message);
			setErrorNotification(message);
		} else {
			setInfoNotificaiton(
				'The airline has been successfully registered in the system'
			);
		}
	};
	const handleCancel = () => {
		navigate('/');
	};

	return {
		initialValues,
		rules,
		handleRegisterAirline,
		handleCancel
	};
};

export default useDependencies;
