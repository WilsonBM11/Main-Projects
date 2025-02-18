import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input } from 'antd';
import './LoginStyles.css';
import useDependencies from './hooks';
import React from 'react';

const App = () => {
	const { handleLogin, handleRegister } = useDependencies();

	return (
		<div className='landingPageContainer'>
			<h1>EasyTravel</h1>
			<div className='loginContainer'>
				<h1>Login</h1>
				<Form
					name='normal_login'
					className='login-form'
					initialValues={{ remember: true }}
					onFinish={handleLogin}
				>
					<Form.Item
						name='username'
						rules={[{ required: true, message: 'Please input your Username!' }]}
					>
						<Input
							prefix={<UserOutlined className='site-form-item-icon' />}
							placeholder='Username'
						/>
					</Form.Item>
					<Form.Item
						name='password'
						rules={[{ required: true, message: 'Please input your Password!' }]}
					>
						<Input
							prefix={<LockOutlined className='site-form-item-icon' />}
							type='password'
							placeholder='Password'
						/>
					</Form.Item>
					<Form.Item>
						<Button
							type='primary'
							htmlType='submit'
							className='login-form-button'
						>
							Log in
						</Button>

						<Button style={{ marginLeft: '10px' }} onClick={handleRegister}>
							Register
						</Button>

					</Form.Item>
				</Form>
			</div>
		</div>
	);
};

export default App;
