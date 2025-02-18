import { useEffect, useState } from 'react';
import useDependencies from './hooks';
import {
	LoadingOutlined,
	PlusCircleOutlined,
	SaveOutlined,
	StopOutlined,
} from '@ant-design/icons';
import { Button, Card, Form, Input, Select, Switch } from 'antd';
import { detailsHotel } from '../../models/hotels.models';
import './registerRoomStyles.css';

const RegisterRoomHotel = () => {
	const { handleRegisterRoom, handleCancel, handleGetRoom } = useDependencies();
	const [loading, setLoading] = useState(false);
	const [hotels, setHotels] = useState<detailsHotel[]>([]);

	useEffect(() => {
		const fetchHotels = async () => {
			const result = await handleGetRoom();
			console.log(result); // Verificar los datos recibidos
			if (result && Array.isArray(result.hotels)) {
				setHotels(result.hotels);
			}
		};
		fetchHotels();
	}, [handleGetRoom]);

	return (
		<div className='roomContainer'>
			{loading ? (
				<div>
					<LoadingOutlined />
				</div>
			) : (
				<Card className='roomComponent'>
					<h1>
						<PlusCircleOutlined /> Room Hotel Register
					</h1>
					<Form
						initialValues={{ remember: true }}
						onFinish={handleRegisterRoom}
						autoComplete='off'
					>
						<Form.Item
							label='Room Number'
							name='roomNumber'
							rules={[{ required: true, message: 'Please input room number' }]}
							className='classFormItem1'
						>
							<Input size='middle' />
						</Form.Item>

						<Form.Item
							label='Room Details'
							name='details'
							rules={[
								{
									required: true,
									message: 'Please input details of the room hotel',
								},
							]}
							className='classFormItem1'
						>
							<Input size='middle' />
						</Form.Item>

						<Form.Item
							label='Room Price'
							name='price'
							rules={[
								{
									required: true,
									message: 'Please input price of the room hotel',
								},
							]}
							className='classFormItem1'
						>
							<Input size='middle' type='number' />
						</Form.Item>

						<Form.Item
							label='Available'
							name='available'
							valuePropName='checked'
							className='classFormItem1'
						>
							<Switch style={{ marginRight: '180px' }} />
						</Form.Item>

						<Form.Item
							label='Hotel'
							name='hotelId'
							rules={[{ required: true, message: 'Please select hotel' }]}
							className='classFormItem1'
						>
							<Select>
								{hotels.map(hotel => (
									<Select.Option key={hotel.hotelId} value={hotel.hotelId}>
										{hotel.name}
									</Select.Option>
								))}
							</Select>
						</Form.Item>

						<Form.Item className='classFormItem1'>
							<Button type='primary' htmlType='submit'>
								Save <SaveOutlined />
							</Button>
							<Button
								type='default'
								onClick={handleCancel}
								style={{ marginLeft: '8px' }}
							>
								Cancel <StopOutlined />
							</Button>
						</Form.Item>
					</Form>
				</Card>
			)}
		</div>
	);
};

export default RegisterRoomHotel;
