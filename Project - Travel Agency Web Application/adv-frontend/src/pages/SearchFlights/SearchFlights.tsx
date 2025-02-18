import React, { useEffect, useRef, useState } from 'react';
import { Range } from 'react-range';
import {
	AutoComplete,
	Card,
	Cascader,
	Checkbox,
	Form,
	Slider,
	Button,
	Modal,
} from 'antd';
import useDependencies from './hooks';
import {
	FlightResponse,
	SearchAirline,
	SearchDestination,
	SearchForm,
} from './types';
import './searchStyles.css';
import { useSessionHandler } from '../../hooks/useSessionHandler';
import EditFlight from '../EditFlight/EditFlight';
import { isNill } from '../../utils/common.utils';
import { tr } from 'date-fns/locale';



const App = () => {
	const {
		handleSearchAirline,
		handleSearchDestination,
		handleDetails,
		handleReservation,
		handleGetAirlines,
		handleGetDestinies,
		destinyOptions,
		optionsAirlines,
		listTransform,
		handleEdit
	} = useDependencies();

	const { sessionContext } = useSessionHandler();

	const [form] = Form.useForm();
	const [results, setResults] = useState<FlightResponse[]>([]);
	const [priceRange, setPriceRange] = useState([0, 1000]);
	const [minPrice, setMinPrice] = useState(0);
	const [maxPrice, setMaxPrice] = useState(1000);
	const [airlineOptions, setAirlineOptions] = useState<string[]>([]);
	const [destinationOptions, setDestinationOptions] = useState<string[]>([]);
	const [departureOptions, setDepartureOptions] = useState<string[]>([]);
	const [selectedAirlines, setSelectedAirlines] = useState(null);
	const [selectedDestinations, setSelectedDestinations] = useState(null);
	const [selectedDepartures, setSelectedDepartures] = useState(null);
	const [searchClicked, setSearchClicked] = useState(false);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const flightIdRef = useRef<string>("");
	

	useEffect(() => {
		

		const FlightSearch = async (values: SearchForm) => {
			
			let result = [];
			setResults([]);
			
			if (!values.destination) {
				const airline: SearchAirline = { airline: values.airline };
				result = await handleSearchAirline(airline);
			} else {
				const destination: SearchDestination = {
					destination: values.destination,
				};
				result = await handleSearchDestination(destination);
			}

			if (result) {
				setResults(result);

				const prices = result.flatMap(flight => [
					flight.firstClassPrice,
					flight.businessClassPrice,
					flight.commercialClassPrice,
				]);
				const airlines = [...new Set(result.map(flight => flight.airline))];
				setAirlineOptions(airlines);
				const destinations = [
					...new Set(result.map(flight => flight.destination)),
				];
				setDestinationOptions(destinations);
				const departures = [...new Set(result.map(flight => flight.departure))];
				setDepartureOptions(departures);
				if (prices.length > 0) {
					const minPrice = Math.min(...prices);
					const maxPrice = Math.max(...prices);
					setMinPrice(minPrice);
					setMaxPrice(maxPrice);
					setPriceRange([minPrice, maxPrice]);
				} else {
					setMinPrice(0);
					setMaxPrice(0);
					setPriceRange([0, 0]);
				}
			} else {
				setMinPrice(0);
				setMaxPrice(0);
				setPriceRange([0, 0]);
			}
		};
		if (searchClicked) {
			FlightSearch(form.getFieldsValue());
			setSearchClicked(false);
		}

		
	}, [form, handleSearchAirline, handleSearchDestination]);

	useEffect(() => {
		const fetchData = async () => {
			handleGetAirlines();
			handleGetDestinies();
		};

		void fetchData();
	}, []);

	const handleSearchClick = () => {
		setSearchClicked(true);
	};

	const showModal = (id:string) => {
		console.log("showModal called with id:", id);
    	flightIdRef.current = id; // Actualiza la referencia mutable
    	console.log("flightIdRef.current updated to:", flightIdRef.current);
    	setIsModalOpen(true); // Abre el modal
	
	};

	const handleOk = () => {
		console.log("showModal close with id:", flightIdRef.current);
		flightIdRef.current = "" ; // Actualiza la referencia mutable
    	console.log("flightIdRef.current updated to:", flightIdRef.current);
		setIsModalOpen(false);
	};

	const handleCancel = () => {
		console.log("showModal close with id:", flightIdRef.current);
		flightIdRef.current = "" ; // Actualiza la referencia mutable
    	console.log("flightIdRef.current updated to:", flightIdRef.current);
		setIsModalOpen(false);
	};

	// Filtrar vuelos basados en el rango de precios
	const filteredResults = results.filter(flight => {
		const { firstClassPrice, businessClassPrice, commercialClassPrice } =
			flight;
		return (
			(firstClassPrice >= priceRange[0] && firstClassPrice <= priceRange[1]) ||
			(businessClassPrice >= priceRange[0] &&
				businessClassPrice <= priceRange[1]) ||
			(commercialClassPrice >= priceRange[0] &&
				commercialClassPrice <= priceRange[1])
		);
	});

	return (
		
		<div className='searchFlightsMainContainer'>
			<div className='searchFlightsForm'>
				<h1>¿Where is your next destiny?</h1>
				<Form form={form} name='searchFlights' style={{ display: 'flex' }}>
					<Form.Item name='airline' label='Airline'>
						<AutoComplete
							className='searchFlightsInput'
							options={optionsAirlines}
							style={{ width: 200 }}
						/>
					</Form.Item>
					<Form.Item
						name='destination'
						label='Destination'
						style={{ margin: '0 1rem' }}
					>
						<AutoComplete
							className='searchFlightsInput'
							options={destinyOptions}
							style={{ width: 200 }}
						/>
					</Form.Item>
					<Form.Item>
						<Button
							type='primary'
							onClick={handleSearchClick}
							style={{ margin: '0 1rem' }}
						>
							Search
						</Button>
					</Form.Item>
				</Form>
			</div>

			{filteredResults.length !== 0 ? (
				<div>
					<div className='filterContainer'>
						<div>
							<h3>Price:</h3>
							<Slider
								range
								defaultValue={[0, maxPrice]} // Establecer los valores por defecto
								value={priceRange} // Usar priceRange para controlar el valor del slider
								onChange={setPriceRange} // Actualizar priceRange cuando cambie el slider
								marks={{
									0: '$0',
									[minPrice]: '$' + minPrice,
									[Math.round((maxPrice + minPrice) / 2)]:
										'$' + Math.round((maxPrice + minPrice) / 2),
									[maxPrice]: '$' + maxPrice,
								}} // Usar minPrice y maxPrice como marcas
								min={0} // Establecer el mínimo como minPrice
								max={maxPrice} // Establecer el máximo como maxPrice
							/>
						</div>
						<div className='checkboxes'>
							<div className='eachCheckbox'>
								<h3>Airline:</h3>
								<Cascader
									options={listTransform(airlineOptions)}
									defaultValue={['All']}
									onChange={selected => {
										if (selected.includes('All')) {
											setSelectedAirlines(null);
										} else {
											setSelectedAirlines(selected);
										}
									}}
									style={{ marginBottom: '1rem' }}
								/>
							</div>
							<div className='eachCheckbox'>
								<h3>Departures:</h3>
								<Cascader
									options={listTransform(departureOptions)}
									defaultValue={['All']}
									onChange={selected => {
										if (selected.includes('All')) {
											setSelectedDepartures(null);
										} else {
											setSelectedDepartures(selected);
										}
									}}
									style={{ marginBottom: '1rem' }}
								/>
							</div>
							<div className='eachCheckbox'>
								<h3>Destinations:</h3>
								<Cascader
									options={listTransform(destinationOptions)}
									defaultValue={['All']}
									onChange={selected => {
										if (selected.includes('Todos')) {
											setSelectedDestinations(null);
										} else {
											setSelectedDestinations(selected);
										}
									}}
									style={{ marginBottom: '1rem' }}
								/>
							</div>
						</div>
					</div>
					<div>
						<ul className='filteredResultsContainer'>
							{filteredResults
								.filter(
									flight =>
										(selectedAirlines === null ||
											selectedAirlines.includes(flight.airline)) &&
										(selectedDestinations === null ||
											selectedDestinations.includes(flight.destination)) &&
										(selectedDepartures === null ||
											selectedDepartures.includes(flight.departure))
								)
								.map(flight => (
									<li key={flight.id}>
										<Card>
											<h1>
												{flight.departure} - {flight.destination}
											</h1>
											<h2>{flight.airline}</h2>
											<p>
												Departure day and hour :{' '}
												{new Date(flight.departureTime).toLocaleString()}
											</p>
											<p>
												Arrival day and hour :{' '}
												{new Date(flight.arrivalTime).toLocaleString()}
											</p>
											<p>Price (First Class): ${flight.firstClassPrice}</p>
											<p>
												Price (Business Class): ${flight.businessClassPrice}
											</p>
											<p>
												Price (Commercial Class): ${flight.commercialClassPrice}
											</p>
											<Button onClick={() => handleDetails(flight.id)}>Details</Button>
											
											{sessionContext?.roles == "ADMIN" ? (
												<Button onClick={() => {showModal(flight.id)}} >
        											Edit
      											</Button>
											)
												: null}
												{sessionContext?.roles != "ADMIN" ? (
												<Button onClick={() => handleReservation(flight.id)}>Reserve</Button>
											)
												: null}

										</Card>
										
									</li>
								))}

										
						</ul>
					</div>
					
				</div>
			) : (
				<p>Sorry, there are no flights to show</p>
			)}
			<Modal title="Edit Flight" open={isModalOpen} onOk={handleOk} onCancel={handleCancel}>
			<EditFlight id={flightIdRef.current}></EditFlight>
			</Modal>
		</div>
	);
};

export default App;
