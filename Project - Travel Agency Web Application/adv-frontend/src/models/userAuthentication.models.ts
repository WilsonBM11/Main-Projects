export type UserAuthentication = {
    email: string;
    password: string;
};
export type UserAuthenticationResponse = {
    token: string;
};

export type UserRegistration = {
    name: string;
    email: string;
    password: string;
};

export type UserRegistrationResponse = {
    response: string;
};