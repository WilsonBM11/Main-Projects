import {
	getSessionToken,
	removeSessionToken,
	setSessionToken,
} from '../services/cookies.service';
import { isNill } from '../utils/common.utils';
import { jwtDecode } from 'jwt-decode';
import { useAppStore } from './store/useAppStore';
import { UserAuthenticationResponse } from '../../src/models/userAuthentication.models';
interface DecodedToken {
	exp: number;
	email: string;
	roles: string;
}

export const useSessionHandler = () => {
	const isSessionExpired = (): boolean => {
		const token = getSessionToken();

		if (isNill(token)) {
			return true;
		} else {
			// eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
			const { exp } = jwtDecode(token as string) as DecodedToken;
			return !(exp > Math.floor(Date.now() / 1000));
		}
	};
	const sessionContext = useAppStore(store => store.session);
	const setSessionContext = useAppStore(store => store.setSession);
	const clear = useAppStore(store => store.clearSession);

	const setSessionStore = (response: UserAuthenticationResponse) => {
		const { token } = response;
		const { email, roles } = jwtDecode(token as string) as DecodedToken;
		setSessionContext({ email: email, roles:roles });
		setSessionToken(token);
	};
	const clearSession = () => {
		clear();
		removeSessionToken();
	};

	const loadSessionFromToken = () => {
		if (!isSessionExpired()) {
			const token = getSessionToken();

			if (token != null) {
				// eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
				const { email, roles } = jwtDecode(token as string) as DecodedToken;
				setSessionContext({ email: email, roles:roles });
			}
		}
	};
	return {
		isSessionExpired,
		sessionContext,
		setSessionStore,
		clearSession,
		loadSessionFromToken,
	};
};
