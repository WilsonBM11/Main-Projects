import {
	Button,
	Card,
	Cascader,
	CascaderProps,
	DatePicker,
	Form,
	Input,
	InputNumber,
	InputNumberProps,
	Modal,
	Popconfirm,
	PopconfirmProps,
	Popover,
} from 'antd';
import {
	CalendarOutlined,
	LoadingOutlined,
	PlusCircleOutlined,
} from '@ant-design/icons';
import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import useDependencies from './hooks';
import { Option } from './types';
import './registerFlightStyles.css';
import RegisterAirline from '../RegisterAirline/RegisterAirline';
import { useSessionHandler } from '../../hooks/useSessionHandler';
dayjs().format('DD/MM/YYYY');

const RegisterFlight = () => {
	const [form] = Form.useForm();

	//deconstruct funcs
	const { handleRegisterFlight, handleCancel, options, handleGetAirlines } = useDependencies();
	const { RangePicker } = DatePicker;

	//useStateVars
	const [airplanes, setAirplanes] = useState<Option[]>();
	const [airline, setAirline] = useState<any>('');
	const [airplane, setAirplane] = useState<any>('');
	const [loading, setLoading] = useState<boolean>(false);
	const [isModalOpen, setIsModalOpen] = useState(false);

	const {sessionContext} = useSessionHandler();

	//funcs

	const showModal = () => {
		
    	setIsModalOpen(true); // Abre el modal
		
		
	};

	const handleOk = () => {
		
		setIsModalOpen(false);
	};

	const handleCancelModal = () => {
	
    	setIsModalOpen(false);
	};

	const onChangeAirplane: CascaderProps<Option>['onChange'] = (value: any) => {
		console.log(value[0] as string);
		setAirplane(value[0] as string);
		console.log(airplane);
	};

	const onChangeAirline: CascaderProps<Option>['onChange'] = (value: any) => {
		console.log(value[0] as string);
		setAirline(value[0] as string);
		console.log(airline);
	};

	const confirm: PopconfirmProps['onConfirm'] = () => {
		form.submit();
	};

	const cancel: PopconfirmProps['onCancel'] = () => {
	};

	useEffect(() => {
		const fetchData = async () => {
			setLoading(true);
			handleGetAirlines();
			setLoading(false);

		};

		void fetchData();
	}, [isModalOpen]);

	const onChangeNumber: InputNumberProps['onChange'] = value => {
		console.log('changed', value);
	};

	const onChangeNumber2: InputNumberProps['onChange'] = value => {
		console.log('changed', value);
	};

	return (
		<div className='registerFlightContainer'>
			{loading ? (
				<div>
					<LoadingOutlined />
				</div>
			) : (
				<>
					<Card className='cardComponent' style={{
						border: '0.3rem solid #4f85a0',
						borderRadius: '1rem',
					}}>
						<Form
							initialValues={{ remember: true }}
							onFinish={handleRegisterFlight}
							autoComplete='off'
							form={form}
						>
							<h1 className='mainTitle'>
								<PlusCircleOutlined />
								Register Flight
							</h1>
							<div style={{ display: 'flex' }}>
								<div>
									<Form.Item
										label={
											<div>
												<CalendarOutlined />
											</div>
										}
										name='dd_datetime'
										rules={[
											{
												required: true,
												message:
													'Please input your departure and destination date!',
											},
										]}
									>
										<RangePicker showTime size='middle' />
									</Form.Item>
								</div>
							</div>
							<Form.Item
								label='Airline'
								name='airline'
								rules={[
									{ required: true, message: 'Please select an airplane' },
								]}
							>
								<Cascader
									options={options}
									onChange={onChangeAirline}
									placeholder='Please select'
									size='middle'
								/>
							</Form.Item>

							<Form.Item
								label='Airplane'
								name='airplane'
								rules={[
									{ required: true, message: 'Please select an airplane' },
								]}
							>
								<Input size='middle' />
							</Form.Item>

							<div style={{ display: 'flex', flexDirection: 'row' }}>
								<Form.Item
									label='Departure'
									name='departure'
									rules={[
										{ required: true, message: 'Please input the departure!' },
									]}
								>
									<Input size='middle' />
								</Form.Item>

								<Form.Item
									label='Destination'
									name='destination'
									rules={[
										{ required: true, message: 'Please input the departure!' },
									]}
								>
									<Input size='middle' />
								</Form.Item>
							</div>

							<Popover title='First Class' trigger='hover' placement='leftTop'>
								<div className='classForm'>
									<Form.Item
										className='classFormItem'
										label='Seats'
										name='firstClassSeats'
										rules={[
											{ required: true, message: 'Please select a number' },
										]}
									>
										<InputNumber
											className='classFormItem'
											min={1}
											size='middle'
										/>
									</Form.Item>

									<Form.Item
										className='classFormItem'
										label='Price'
										name='firstClassPrice'
										rules={[
											{ required: true, message: 'Please select a number' },
										]}
									>
										<InputNumber
											className='classFormItem'
											min={1}
											prefix='$'
											size='middle'
										/>
									</Form.Item>
								</div>
							</Popover>

							<Popover title='Second Class' trigger='hover' placement='leftTop'>
								<div className='classForm'>
									<div>
										<Form.Item
											className='classFormItem'
											label='Seats'
											name='secondClassSeats'
											rules={[
												{ required: true, message: 'Please select a number' },
											]}
										>
											<InputNumber
												className='classFormItem'
												min={1}
												size='middle'
											/>
										</Form.Item>
									</div>

									<div>
										<Form.Item
											className='classFormItem'
											label='Price'
											name='secondClassPrice'
											rules={[
												{ required: true, message: 'Please select a number' },
											]}
										>
											<InputNumber
												className='classFormItem'
												min={1}
												prefix='$'
												size='middle'
											/>
										</Form.Item>
									</div>
								</div>
							</Popover>

							<Popover title='Third Class' trigger='hover' placement='leftTop'>
								<div className='classForm'>
									<div>
										<Form.Item
											className='classFormItem'
											label='Seats'
											name='thirdClassSeats'
											rules={[
												{ required: true, message: 'Please select a number' },
											]}
										>
											<InputNumber
												className='classFormItem'
												min={1}
												size='middle'
											/>
										</Form.Item>
									</div>

									<div>
										<Form.Item
											className='classFormItem'
											label='Price'
											name='thirdClassPrice'
											rules={[
												{ required: true, message: 'Please select a number' },
											]}
										>
											<InputNumber
												className='classFormItem'
												min={1}
												prefix='$'
												size='middle'
											/>
										</Form.Item>
									</div>
								</div>
							</Popover>
							<div style={{display:"flex"}}> 
								<Popconfirm
									title='Register flight'
									description='Are you sure you want to register this flight?'
									onConfirm={confirm}
									onCancel={cancel}
									okText='Yes'
									cancelText='No'
								>
									<Button type='primary'>Submit</Button>
								</Popconfirm>
								{sessionContext?.roles == "ADMIN" ? 
								(
									<Button onClick={showModal}>Create Airline</Button>	
								) : 
								null}
								
							</div>
							

						</Form>
					</Card>
					
					<Modal title="Create Airline" open={isModalOpen} onOk={handleOk} onCancel={handleCancelModal}>
						<RegisterAirline></RegisterAirline>
					</Modal>
				</>
			)}
		</div>
	);
};
export default RegisterFlight;
