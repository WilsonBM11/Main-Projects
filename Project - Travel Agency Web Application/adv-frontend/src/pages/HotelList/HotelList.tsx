import React, { useEffect, useState } from 'react';
import { Card, Space, Button, Input, Row, Col, AutoComplete } from 'antd';
import { HotelResponse, detailsHotel } from '../../models/hotels.models';
import useDependencies from './hooks';
import './HotelStyle.css';
import { RightOutlined } from '@ant-design/icons';

const HotelList = () => {
	const {
		handleGetAccepted,
		handleDetails,
		handleSearchHotelByName,
		handleSearchHotelByLocation,
	} = useDependencies();
	const [hotels, setHotel] = useState<HotelResponse[]>([]);
	const [searchTerm, setSearchTerm] = useState<string>('');
	const [locationTerm, setLocationTerm] = useState<string>('');
	const [filteredHotels, setFilteredHotels] = useState<HotelResponse[]>([]);
	const uniqueAddresses = [...new Set(hotels.map(hotel => hotel.address))];
	const uniqueName = [...new Set(hotels.map(hotel => hotel.name))];

	// Listas de nombres y ubicaciones sugeridas
	useEffect(() => {
		const getHotels = async () => {
			try {
				const response = await handleGetAccepted();
				if (Array.isArray(response)) {
					setHotel(response);
					setFilteredHotels(response); // Inicializa los hoteles filtrados con todos los hoteles
				}
			} catch (error) {
				console.log(error);
			}
		};

		void getHotels();
	}, []);

	const handleSearch = async () => {
		if (searchTerm.trim() === '' && locationTerm.trim() === '') {
			return; // No hace nada si ambos campos de búsqueda están vacíos
		}

		try {
			let result:
				| detailsHotel[]
				| ((prevState: HotelResponse[]) => HotelResponse[]) = [];
			if (searchTerm.trim() !== '') {
				result = await handleSearchHotelByName(searchTerm);
			} else if (locationTerm.trim() !== '') {
				result = await handleSearchHotelByLocation(locationTerm);
			}
			setFilteredHotels(result);
		} catch (error) {
			console.error('Error searching hotels:', error);
		}
	};

	return (
		<div className='hotel-details-container'>
			<div className='search-container' style={{ marginBottom: 20 }}>
				<h1>Find your next stay!</h1>
				<Row gutter={16}>
					<Col>
						<AutoComplete
							style={{ width: 200 }}
							options={uniqueName.map(hotel => ({ value: hotel }))}
							onSelect={value => setSearchTerm(value)}
							onSearch={value => setSearchTerm(value)}
							placeholder='Search for name hotel'
						>
							<Input />
						</AutoComplete>
					</Col>
					<Col>
					
						<AutoComplete
							style={{ width: 200 }}
							options={uniqueAddresses.map(hotel => ({ value: hotel}))}
							onSelect={value => setLocationTerm(value)}
							onSearch={value => setLocationTerm(value)}
							placeholder='Search for location hotel'
						>
							<Input />
						</AutoComplete>
					</Col>
					<Col>
						<Button type='primary' onClick={handleSearch}>
							Search
						</Button>
					</Col>
				</Row>
			</div>
			<div className='hotel-list'>
				{filteredHotels.length === 0 ? (
					<Card className='hotel-card no-hotels-card'>
						<h3 className='no-hotels'>No hotels available</h3>
					</Card>
				) : (
					filteredHotels.map(hotel => (
						<div key={hotel.hotelId} className='hotel-card-container'>
							<Card title={hotel.name} className='hotel-card'>
								<p style={{ fontWeight: 'bold' }}>Address: {hotel.address}</p>
								<p>Email: {hotel.email}</p>
								<p>
									{hotel.facilities.map((facility, facilityIndex) => (
										<div key={facilityIndex}>{facility}</div>
									))}
								</p>
								<Button
									type='primary'
									onClick={() => handleDetails(hotel.hotelId)} // Pasar el hotelId al hacer clic en el botón
								>
									More details
									<RightOutlined />
								</Button>
							</Card>
						</div>
					))
				)}
			</div>
		</div>
	);
};

export default HotelList;
