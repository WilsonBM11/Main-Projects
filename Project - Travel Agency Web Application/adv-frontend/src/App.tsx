import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { Alert } from 'antd';
import Login from './pages/LoginUsers/Login';
import Main from './pages/Main/Main';
import Register from './pages/RegisterUser/Register';
import Search from './pages/SearchFlights/SearchFlights';
import { useSessionHandler } from './hooks/useSessionHandler';
import { useMemo } from 'react';
import Home from './pages/Home/Home';
import NotFound from './pages/NotFound/NotFound';
import HotelDetails from './pages/HotelDetails/HotelDetails';
import RegisterHotel from './pages/RegisterHotel/RegisterHotel';
import RegisterFlight from './pages/RegisterFlight/RegisterFlight';
import UserReservations from './pages/FlightReservations/FlightReservations';
import RegisterRoomHotel from './pages/RegisterHotelRoom/RegisterHotelRoom';
import FlightDetails from './pages/FlightDetails/FlightDetails';
import FlightSeatReservation from './pages/FlightSeatReservation/FlightSeatReservation';
import HotelList from './pages/HotelList/HotelList';
import ReservationsHotelList from './pages/ReservationsHotelList/ReservationsHotelList';


const { ErrorBoundary } = Alert;
const publicRoute = () => {
	return (
		<Routes>
			<Route path='/' element={<Main />}>
				<Route index element={<Login />} />
				<Route path='/register' element={<Register />} />
			</Route>
		</Routes>
	);
};
const privateRoute = () => {
	return (
		<Routes>
			<Route path='/' element={<Main />}>
				<Route index element={<Home />} />
				<Route path='/hotels' element={<HotelList />}></Route>
				<Route path='/flights' element={<Search />}></Route>
				<Route path='/hotelDetail/:hotelId' element={<HotelDetails />}></Route>
				<Route path='/reservations' element={<UserReservations />}></Route>
				<Route path='/flights/:flightId' element={<FlightDetails />}></Route>
				<Route path='/flights/reserve/:flightId' element={<FlightSeatReservation />}></Route>
				<Route path='/hotels/reservations' element={<ReservationsHotelList />}></Route>
				<Route path='*' element={<NotFound></NotFound>}></Route>
			</Route>
		</Routes>
	);
};

const AdminRoute = () => {
	return (
		<Routes>
			<Route path='/' element={<Main />}>
				<Route index element={<Home />} />
				<Route path='/hotelDetail/:hotelId' element={<HotelDetails />}></Route>
				<Route path='/registerFlight' element={<RegisterFlight></RegisterFlight>}></Route>
				<Route path='/flights' element={<Search />}></Route>
				<Route path='/registerHotel' element={<RegisterHotel></RegisterHotel>}></Route>
				<Route path='/reservations' element={<UserReservations />}></Route>
				<Route path='/hotels' element={<HotelList />} />
				<Route path='/hotelDetail/:hotelId' element={<HotelDetails />}></Route>
				<Route path='/registerHotel' element={<RegisterHotel />}></Route>
				<Route path='/registerRoom' element={<RegisterRoomHotel />}></Route>
				<Route path='/registerFlight' element={<RegisterFlight></RegisterFlight>}>
					{' '}
				</Route>
				<Route path='/flights' element={<Search />}></Route>'
				<Route path='/flights/:flightId' element={<FlightDetails />}></Route>
				<Route path='*' element={<NotFound></NotFound>}></Route>
			</Route>
		</Routes>
	);
};

const App = () => {
	const { sessionContext, loadSessionFromToken } = useSessionHandler();
	useMemo(() => {
		if (sessionContext == null) {
			loadSessionFromToken();
		}
	}, []);

	return (
		<ErrorBoundary
			description='Something when wrong, please contact an administrator'
			message='An unknown error ocurrs'
		>
			<BrowserRouter>
				{sessionContext == null
					? publicRoute()
					: sessionContext?.roles == 'ADMIN'
					? AdminRoute()
					: privateRoute()}
			</BrowserRouter>
		</ErrorBoundary>
	);
};

export default App;
