import { useEffect, useRef, useState } from 'react';
import useDependencies from './hooks';
import { Button, Card, Modal } from 'antd';
import './reservationStyles.css';
import useNotificationHandler from '../../hooks/useNotificationHandler';
import React from 'react';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

const App = () => {

	const seatIdRef = useRef<string>("");
	const [reservations, setReservations] = useState<FlightReservationResponse[]>(
		[]
	);
	const [isModalOpen, setIsModalOpen] = useState(false);

	const showModal = (id:string) => {
		seatIdRef.current = id;
		setIsModalOpen(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
	};

	const Date = (date: string) => {
		const Dates = format(date, "d 'de' MMMM 'de' yyyy h:mm aa", { locale: es });
		return Dates;
	};

	const { setErrorNotification } = useNotificationHandler();
	const cancelReservation = (seatId: string) => {
		console.log("Asiento"+ seatId);
		setIsModalOpen(false);
		handleCancelReservation(seatId);
		setErrorNotification('Reservation cancelled!');
		setTimeout(() => {
			window.location.reload();
		}, 5000);
	};

	useEffect(() => {
		const userReservation = async () => {
			const result = await handleUserReservation();
			if (result) {
				setReservations(result);
			}
			console.log(result);
		};
		userReservation();
	}, []);

	const { handleUserReservation, handleCancelReservation } = useDependencies();

	return (
		<div className='userReservationsContainer'>
			<h1>Reservations</h1>
			<div className='reservations' style={{ margin: '10px 20px' }}>
				{reservations.length > 0 ? (
					reservations.map((reservation, index) => (
						<Card
							key={index}
							title={
								reservation.airline +
								': ' +
								reservation.departure +
								'-' +
								reservation.destination
							}
						>
							<p>{'Departure: ' + Date(reservation.departureTime)}</p>
							<p> {'Arrival: ' + Date(reservation.arrivalTime)}</p>

							<div className='eachTicketContainer'>
								{reservation.seats.map((seat, index) => (
									<Card style={{ width: '200px', margin: '10px 20px' }}>
										<div key={index} className='seat'>
											<p>Passenger Name: {seat.passengerName}</p>
											<p>Seat Class: {seat.seatClass}</p>
											<p>Seat Price: ${seat.seatPrice}</p>
											<p>Seat State: {seat.seatState}</p>
										</div>
										<Button type='primary' onClick={() => showModal(seat.seatId)} danger>
											Cancel
										</Button>
										<Modal
											title='Are you sure you want to cancel this reservation?'
											open={isModalOpen}
											onOk={() => cancelReservation(seatIdRef.current)}
											onCancel={handleCancel}
											okText='Yes'
											cancelText='No'
											okType='danger'
										>
											<p>This can't be undone.</p>
										</Modal>
									</Card>
								))}
							</div>
						</Card>
					))
				) : (
					<p>No hay reservaciones</p>
				)}
			</div>
		</div>
	);
};

export default App;
