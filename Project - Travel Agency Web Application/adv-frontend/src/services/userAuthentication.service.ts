import { doGet, doPost, doPut } from './http.service';
import { UserAuthentication,UserAuthenticationResponse, UserRegistration, UserRegistrationResponse } from '../models/userAuthentication.models';

export const login = async (user: UserAuthentication): Promise<UserAuthenticationResponse> => {
    const result = await doPost<UserAuthentication, UserAuthenticationResponse>(
        user,
        '/api/public/auth/login'
    );
    return result;
}

export const register = async (
    user: UserRegistration
): Promise<UserRegistrationResponse> => {
    const result = await doPost<UserRegistration, UserRegistrationResponse>(
        user,
        '/api/public/auth/register'
    );
    return result;
}