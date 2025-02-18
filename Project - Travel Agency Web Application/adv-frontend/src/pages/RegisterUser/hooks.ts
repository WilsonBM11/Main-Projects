import { UserRegistration, UserAuthenticationResponse } from '../../models/userAuthentication.models';
import { register } from '../../services/userAuthentication.service';
import { validationSchema } from '../../utils/forms.util';
import { RegisterForm } from './types';
// import { ErrorResponse } from '@/models/api.models';
import { useNavigate } from 'react-router-dom';
import { useSessionHandler } from '../../hooks/useSessionHandler';
import { useApiHandler } from '../../hooks/useApiHandler';
import useNotificationHandler from '../../hooks/useNotificationHandler';


const useDependencies = () => {
	const { clearSession } = useSessionHandler();
	const { handleMutation } = useApiHandler();
	const { setErrorNotification } = useNotificationHandler();

	
	const navigate = useNavigate();

	const initialValues = {
		username: '',
		password: '',
	};
	const rules = {
		username: [validationSchema.requiredFieldRule],
		password: [validationSchema.requiredFieldRule],
	};

	const handleRegister = async (values: RegisterForm) => {
		const user: UserRegistration = {
			name: values.username,
            email: values.email,
			password: values.password,
		};

		//const { response,failed,success } = await postLogin(user);
		const { isError, message } = await handleMutation(register,user);
		if (!isError) {
			navigate('/');
			console.log('Registration successful');
		} else {
			setErrorNotification(message);
			console.log('Registration failed');
		}
	};
	const handleCancel = () => {
		 navigate('/');
	};

	return { initialValues, rules, handleRegister, handleCancel };
};
export default useDependencies;

