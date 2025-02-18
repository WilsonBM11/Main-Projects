import {
	Button,
	Card,
	Form,
	Input,
	Popconfirm,
	PopconfirmProps,
} from 'antd';

import './RegisterAirline.css';
import useDependencies from './hooks';
import { useState } from 'react';


const RegisterAirline = () => {

	const [form] = Form.useForm();

	const { handleRegisterAirline } =
		useDependencies();

	const confirm: PopconfirmProps['onConfirm'] = () => {
		form.submit();
	};

	const cancel: PopconfirmProps['onCancel'] = () => {
	};

	return (

		<div className='registerAirlineContainer'>
			<h1>Register Airline</h1>
			<>
				<Card className='cardComponent' style={{
					border: '0.3rem solid #4f85a0',
					borderRadius: '1rem',
				}}>
					<Form
						initialValues={{ remember: true }}
						onFinish={handleRegisterAirline}
						autoComplete='off'
						form={form}
					>
						<Form.Item
							label='Airline name'
							name='airline'
							rules={[
								{
									required: true,
									message: 'Please input the airline name!',
								},
							]}
						>
							<Input />
						</Form.Item>


						<Popconfirm
							title='Register Airline'
							description='Are you sure you want to register this airline?'
							onConfirm={confirm}
							onCancel={cancel}
							okText='Yes'
							cancelText='No'
						>
							<Button type='primary'>Submit</Button>
						</Popconfirm>
					</Form>
				</Card>
			</>

		</div>
	);
};
export default RegisterAirline;
