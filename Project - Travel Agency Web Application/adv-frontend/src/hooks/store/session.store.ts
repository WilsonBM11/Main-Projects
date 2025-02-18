export type Session = {
	email: string;
	roles:string;
};

export type SessionStore = {
	session: Session | null;
	setSession: (session: Session) => void;
	clearSession: () => void;
};

export const sessionStore = (set: any) => {
	return {
		session: null,
		setSession: (session: Session) => {
			set({ session });
		},
		clearSession: () => {
			set({ session: null });
		},
	};
};
