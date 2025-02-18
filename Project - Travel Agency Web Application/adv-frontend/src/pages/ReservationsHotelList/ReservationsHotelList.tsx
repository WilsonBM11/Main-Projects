import React, { useEffect, useState } from 'react';
import { List, Card, Button, Modal, Divider, Empty } from 'antd'; // Importa Empty desde antd
import { ReservationsRoomsResponse } from '../../models/hotels.models';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';
import useDependencies from './hooks';
import './reservationHotelListStyle.css';

const ReservationsHotelList = () => {
	const { handleGetAccepted, handleCancelReservation } = useDependencies();
	const [reservations, setReservations] = useState<ReservationsRoomsResponse[]>(
		[]
	);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [selectedReservationId, setSelectedReservationId] = useState<
		string | null
	>(null);

	const showModal = (reservationId: string) => {
		setSelectedReservationId(reservationId);
		setIsModalOpen(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
		setSelectedReservationId(null);
	};

	const handleConfirmCancel = async () => {
		if (selectedReservationId) {
			await handleCancelReservation(selectedReservationId);
			setIsModalOpen(false);
			setSelectedReservationId(null);
			const response = await handleGetAccepted();
			if (Array.isArray(response)) {
				setReservations(response);
			}
		}
	};

	const formatDate = (dateStr: string | number | Date) => {
		const date = new Date(dateStr);
		return format(date, "d 'de' MMMM 'de' yyyy h:mm aa", { locale: es });
	};

	useEffect(() => {
		const getReservations = async () => {
			try {
				const response = await handleGetAccepted();
				if (Array.isArray(response)) {
					setReservations(response);
				}
			} catch (error) {
				console.error('Error fetching reservations:', error);
			}
		};

		getReservations();
	}, [handleGetAccepted]);

	// Renderizado personalizado cuando no hay reservaciones
	const renderEmpty = () => (
		<div className='no-reservations-container'>
			<Card className='no-reservations-card'>
				<h2>No reservations available</h2>
			</Card>
		</div>
	);

	return (
		<div className='reservations-container'>
			<h1 className='reservations-title'>Hotel Reservations</h1>

			{/* Control explícito del renderizado de la lista */}
			{reservations.length === 0 ? (
				renderEmpty()
			) : (
				<List
					className='reservations-list'
					dataSource={reservations}
					renderItem={reservation => (
						<List.Item key={reservation.reservationId}>
							<Card
								className={`reservation-card ${
									reservation.canceled ? 'canceled' : ''
								}`}
							>
								<div className='reservation-card-content'>
									<Card.Meta
										title={
											<span className='reservation-card-title'>
												{reservation.roomId.hotelId.name}
											</span>
										}
										description={
											<div>
												<h2>Room Number: {reservation.roomId.roomNumber}</h2>
												<div>
													<h3>
														Start Date: {formatDate(reservation.startDate)}
													</h3>
													<h3>End Date: {formatDate(reservation.endDate)}</h3>
												</div>
												{reservation.canceled ? (
													<Divider>
														<h4>Reservation Canceled</h4>
													</Divider>
												) : (
													<Button
														type='primary'
														onClick={() => showModal(reservation.reservationId)}
														danger
														className='cancel-button'
													>
														Cancel Reservation
													</Button>
												)}

												<Modal
													title='Are you sure you want to cancel this reservation?'
													open={isModalOpen}
													onOk={handleConfirmCancel}
													onCancel={handleCancel}
													okText='Yes'
													cancelText='No'
													okType='danger'
												>
													<p>This action is not reversible.</p>
												</Modal>
											</div>
										}
									/>
								</div>
							</Card>
						</List.Item>
					)}
				/>
			)}
		</div>
	);
};

export default ReservationsHotelList;
