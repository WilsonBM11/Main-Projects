import { useParams } from 'react-router-dom';
import useDependencies from './hooks';
import { useEffect, useState, useCallback } from 'react';
import { HotelResponse, RoomResponse } from '../../models/hotels.models';
import './hotelDetailsStyles.css';
import {
	Button,
	Card,
	Checkbox,
	DatePicker,
	Form,
	Input,
	Modal,
	Slider,
} from 'antd';
import { useSessionHandler } from '../../hooks/useSessionHandler';

const HotelDetail = () => {
	const { handleGetAccepted, handleGetRooms, handleReservations, rules } =
		useDependencies();
	const { hotelId } = useParams<{ hotelId: string }>();

	const [hotel, setHotel] = useState<HotelResponse | null>(null);
	const { sessionContext } = useSessionHandler();
	const [rooms, setRooms] = useState<RoomResponse[]>([]);
	const [filteredRooms, setFilteredRooms] = useState<RoomResponse[]>([]);
	const [dataLoaded, setDataLoaded] = useState(false);
	const [priceRange, setPriceRange] = useState<[number, number]>([100, 700]);
	const [selectedRoom, setSelectedRoom] = useState<RoomResponse | null>(null);

	const [form] = Form.useForm();
	const [isModalOpen, setIsModalOpen] = useState(false);

	const showModal = (room: RoomResponse) => {
		setSelectedRoom(room);
		setIsModalOpen(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
		setSelectedRoom(null);
	};

	const getHotelDetails = useCallback(async () => {
		if (hotelId) {
			try {
				const response = await handleGetAccepted(hotelId);
				if (response) {
					setHotel(response);
				}
			} catch (error) {
				console.error(error);
			}
		}
	}, [hotelId, handleGetAccepted]);

	const getHotelRooms = useCallback(async () => {
		if (hotelId) {
			try {
				const response = await handleGetRooms(hotelId);
				if (response) {
					setRooms(response);
					setFilteredRooms(response);
				}
			} catch (error) {
				console.error(error);
			}
		}
	}, [hotelId, handleGetRooms]);

	useEffect(() => {
		if (hotelId && !dataLoaded) {
			getHotelDetails();
			getHotelRooms();
			setDataLoaded(true);
		}
	}, [hotelId, dataLoaded, getHotelDetails, getHotelRooms]);

	useEffect(() => {
		const filtered = rooms.filter(
			room => room.price >= priceRange[0] && room.price <= priceRange[1]
		);
		setFilteredRooms(filtered);
	}, [priceRange, rooms]);

	if (!hotel) {
		return <div>Loading...</div>;
	}

	return (
		<div className='hotel-details-container'>
			<h2>{hotel.name}</h2>
			<p>Address: {hotel.address}</p>
			<p>Email: {hotel.email}</p>
			<div className='facilities'>
				{hotel.facilities.map((facility, index) => (
					<div key={index} className='facility'>
						{facility}
					</div>
				))}
			</div>

			<div className='price-filter'>
				<h3>Filter by Price</h3>
				<Slider
					range
					min={100}
					max={700}
					defaultValue={[100, 700]}
					onChange={value => setPriceRange(value as [number, number])}
				/>
				<p>
					Selected Price Range: ${priceRange[0]} - ${priceRange[1]}
				</p>
			</div>

			<div className='rooms-list'>
				{filteredRooms.length === 0 ? (
					<Card className='rooms-card no-rooms-card'>
						<h3 className='no-rooms'>No rooms available</h3>
					</Card>
				) : (
					filteredRooms.map(room => (
						<div key={room.roomId} className='room-container'>
							<Card className='rooms-card'>
								<h1>Room Number: {room.roomNumber}</h1>
								<p>Details: {room.details}</p>
								<p>Price: {room.price}</p>
								<Form.Item>
									Available: <Checkbox checked={room.available} disabled />
								</Form.Item>
								{sessionContext?.roles !== 'ADMIN' && (
									<Button type='primary' onClick={() => showModal(room)}>
										Reserve
									</Button>
								)}
							</Card>
						</div>
					))
				)}
			</div>

			<Modal
				title='Reserve Room'
				open={isModalOpen}
				onOk={() => form.submit()}
				onCancel={handleCancel}
				okText='Reserve'
				cancelText='Cancel'
				okType='primary'
			>
				<Form form={form} className='reserve' onFinish={handleReservations}>
					<Form.Item name='roomId' hidden initialValue={selectedRoom?.roomId}>
						<Input />
					</Form.Item>
					<Form.Item
						label='Email Address'
						name='userEmail'
						rules={rules.userEmail}
					>
						<Input placeholder='e.g., example@email.com' />
					</Form.Item>
					<div
						style={{
							display: 'flex',
							justifyContent: 'space-between',
						}}
					>
						<Form.Item
							label='Start Date'
							name='startDate'
							rules={rules.startDate}
						>
							<DatePicker
								format='DD/MM/YYYY'
								style={{ width: '90%' }}
								placeholder='e.g., 11/05/2024'
							/>
						</Form.Item>
						<Form.Item label='End Date' name='endDate' rules={rules.endDate}>
							<DatePicker
								format='DD/MM/YYYY'
								style={{ width: '90%' }}
								placeholder='e.g., 11/05/2024'
							/>
						</Form.Item>
					</div>
				</Form>
			</Modal>
		</div>
	);
};

export default HotelDetail;
