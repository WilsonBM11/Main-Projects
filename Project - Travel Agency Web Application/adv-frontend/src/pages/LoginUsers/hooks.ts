import { UserAuthentication, UserAuthenticationResponse } from '../../models/userAuthentication.models';
import { login } from '../../services/userAuthentication.service';
import { validationSchema } from '../../utils/forms.util';
import { LoginForm } from './types';
import { useNavigate } from 'react-router-dom';
import { useSessionHandler } from '../../hooks/useSessionHandler';
import { useApiHandler } from '../../hooks/useApiHandler';
import useNotificationHandler from '../../hooks/useNotificationHandler';


const useDependencies = () => {
	const { setSessionStore, clearSession } = useSessionHandler();
	const { handleMutation } = useApiHandler();
	const { setErrorNotification, setInfoNotificaiton } = useNotificationHandler();

	
	const navigate = useNavigate();

	const initialValues = {
		username: '',
		password: '',
	};
	const rules = {
		username: [validationSchema.requiredFieldRule],
		password: [validationSchema.requiredFieldRule],
	};

	const handleLogin = async (values: LoginForm) => {
		const user: UserAuthentication = {
			email: values.username,
			password: values.password,
		};
		clearSession();

		//const { response,failed,success } = await postLogin(user);
		const { result, isError, message } = await handleMutation(login, user);

		
		if (isError) {
			console.log(message);
			setErrorNotification(message);
            
		} else {
			const response = result as UserAuthenticationResponse;
			setSessionStore({ ...response });
			setInfoNotificaiton("Welcome!")
			navigate('/');
		}
	};
	const handleRegister = () => {
		 navigate('/register');
	};

	return { initialValues, rules, handleLogin, handleRegister };
};

export default useDependencies;
