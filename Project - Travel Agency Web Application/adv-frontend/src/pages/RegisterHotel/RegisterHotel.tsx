import React, { useState } from 'react';
import { Button, Card, Form, Input } from 'antd';
import {
	LoadingOutlined,
	PlusOutlined,
	MinusCircleOutlined,
	SaveOutlined,
	StopOutlined,
	PlusCircleOutlined,
} from '@ant-design/icons';
import useDependencies from './hooks';
import './registerHotelStyle.css';

const RegisterHotel = () => {
	const { handleRegisterHotel, handleCancel, alert } = useDependencies();
	const [loading, setLoading] = useState(false);

	return (
		<div className='hotelRegisterContainer'>
			{alert && <div style={styles.alertContainer}>{alert}</div>}
			{loading ? (
				<div>
					<LoadingOutlined />
				</div>
			) : (
				<>
					<Card
						className='hotelRegisterComponent'
						style={{
							display: 'flex',
							alignItems: 'center',
							justifyContent: 'center',
						}}
					>
						<h1>
							<PlusCircleOutlined /> Hotel Register
						</h1>

						<Form
							initialValues={{ remember: true }}
							onFinish={handleRegisterHotel}
							autoComplete='off'
							className='classForm1'
						>
							<Form.Item
								label='Name'
								name='name'
								rules={[
									{
										required: true,
										message: 'Please input the name of the hotel',
									},
								]}
								className='classFormItem1'
							>
								<Input size='middle' />
							</Form.Item>

							<Form.Item
								label='Email'
								name='email'
								rules={[
									{
										required: true,
										message: 'Please input the email of the hotel',
									},
								]}
								className='classFormItem1'
							>
								<Input size='middle' />
							</Form.Item>

							<Form.Item
								label='Address'
								name='address'
								rules={[
									{
										required: true,
										message: 'Please input the address of the hotel',
									},
								]}
								className='classFormItem1'
							>
								<Input size='middle' />
							</Form.Item>

							<Form.Item
								label='Phone Number'
								name='phoneNumber'
								rules={[
									{
										required: true,
										message: 'Please input the phone number of the hotel',
									},
								]}
								className='classFormItem1'
							>
								<Input size='middle' type='number' />
							</Form.Item>

							<Form.Item className='classFormItem1'>
								<label className='facilitiesLabel'>Facilities</label>
								<Form.List name='facilities'>
									{(fields, { add, remove }) => (
										<div>
											{fields.map(({ key, name, ...restField }) => (
												<div key={key} className='hotelFacilities'>
													<Form.Item
														{...restField}
														name={name}
														rules={[
															{
																required: true,
																message: 'Please input the facility',
															},
														]}
													>
														<Input placeholder='Facility' />
													</Form.Item>
													<MinusCircleOutlined
														onClick={() => remove(name)}
														style={{
															fontSize: '2rem',
															color: 'red',
															marginBottom: '50%',
														}}
													/>
												</div>
											))}
											<Form.Item>
												<Button
													type='dashed'
													onClick={() => add()}
													block
													icon={<PlusOutlined />}
												>
													Add Facility
												</Button>
											</Form.Item>
										</div>
									)}
								</Form.List>
							</Form.Item>

							<Form.Item
								className='classFormItem1'
								style={{ textAlign: 'center' }}
							>
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
				</>
			)}
		</div>
	);
};

const styles = {
	alertContainer: {
		position: 'fixed' as 'fixed',
		top: 0,
		left: '50%',
		transform: 'translateX(-50%)',
		width: '80%',
		zIndex: 1000,
		display: 'flex',
		justifyContent: 'center',
		alignItems: 'center',
		marginTop: '13%',
	},
};

export default RegisterHotel;
