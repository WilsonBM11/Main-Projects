import React, { useEffect, useState } from 'react';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import Slider from 'react-slick';
import { FlightResponse, HotelResponse } from './types';
import useDependencies from './hooks';
import { Card } from 'antd';
import './HomeStyles.css';	

const App = () => {
	const [flights, setFlights] = useState<FlightResponse[]>([]);
	const [hotels, setHotels] = useState<HotelResponse[]>([]);

	const { handleRandomFlights, handleRandomHotels } = useDependencies();

	useEffect(() => {
		const getFlights = async () => {
			const result = await handleRandomFlights();
			if (result) {
				setFlights(result);
			}
		};

		const getHotels = async () => {
			const result = await handleRandomHotels();
			if (result) {
				setHotels(result);
			}
		};

		getFlights();
		getHotels();
	}, []);


	var settings = {
		dots: true,
		infinite: true,
		speed: 250,
		slidesToShow: 5,
		slidesToScroll: 1,
        autoplay: true,
        pauseOnHover: true,
	};

	return (
        <>
		<h1 style={{textAlign:'center'}}>Welcome to EasyTours</h1>
        <h1>Available Flights</h1>
		<div className='eachSlider'>
			<Slider {...settings}>
				{flights.map(d => (
                    <Card className='cards'>
					<div key={d.id} className='eachFlight'>
						<h1>
							{d.departure} - {d.destination}
						</h1>
						<h2>{d.airline}</h2>
						<p>
							Departure day and hour: {new Date(d.departureTime).toLocaleString()}
						</p>
						<p>
							Arrival day and hour: {new Date(d.arrivalTime).toLocaleString()}
						</p>
						<p>Price (First Class): ${d.firstClassPrice}</p>
						<p>Price (Business Class): ${d.businessClassPrice}</p>
						<p>Price (Commercial Class): ${d.commercialClassPrice}</p>
					</div>
                </Card>
				))}
			</Slider>
		</div>
		<h1>Available hotels</h1>
		<div className='eachSlider'>
			<Slider  className='sliders' {...settings}>
				{hotels.map(d => (
                   <Card className='cards'>
					<h1>
							{d.name}
					</h1>
				   <p>Address: {d.address}</p>
				   <p>
					Facilities:
					   {d.facilities.map((facility, facilityIndex) => (
						   <div key={facilityIndex}>{facility}</div>
					   ))}
				   </p>
			   </Card>
				))}
			</Slider>
		</div>
        </>
	);
};

export default App;
