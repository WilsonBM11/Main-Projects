import { create } from 'zustand';
import { NotificationStore, notificationStore } from './notification.store';
import { SessionStore, sessionStore } from './session.store';

type AppStore = NotificationStore & SessionStore;

export const useAppStore = create<AppStore>(set => ({
	...notificationStore(set),
	...sessionStore(set),
}));
