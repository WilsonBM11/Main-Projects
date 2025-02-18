import { LockOutlined, UserOutlined, MailOutlined } from '@ant-design/icons';
import { Button, Checkbox, Form, Input } from 'antd';
import { useEffect, useState } from 'react';
import useDependencies from './hooks';
import './RegisterStyles.css';

const App = () => {
	const { handleRegister,rules,handleCancel } = useDependencies();

	const [form] = Form.useForm();
	const [formDisabled, setFormDisabled] = useState<boolean>(false);

	const onFinish = (values: any) => {
		console.log('Received values of form: ', values);
	};

	useEffect(() => {
		setFormDisabled(true);
	}, []);

	return (
		<div className='landingPageContainer'>
			<h1>EasyTravel</h1>
			<div className='registerContainer'>
				<h1>Register</h1>
				<Form
					form={form}
					name='normal_login'
					className='login-form'
					initialValues={{ remember: true }}
					onFinish={handleRegister}
				>
					<Form.Item
						name='username'
						rules={[{ required: true, message: 'Please input your Username!' }]}
					>
						<Input
							prefix={<UserOutlined className='site-form-item-icon' />}
							placeholder='Name'
						/>
					</Form.Item>
					<Form.Item
						name='email'
						rules={[{ required: true, message: 'Please input your Username!' }]}
					>
						<Input
							prefix={<MailOutlined className='site-form-item-icon' />}
							placeholder='Email'
						/>
					</Form.Item>
					<Form.Item
						name='password'
						rules={[{ required: true, message: 'Please input your Password!' }]}
					>
						<Input.Password
							prefix={<LockOutlined className='site-form-item-icon' />}
							type='password'
							placeholder='Password'
						/>
					</Form.Item>
					<Form.Item
						name='passwordConfirm'
						rules={[
							{ required: true, message: 'Please confirm your Password!' },
						]}
					>
						<Input.Password
							prefix={<LockOutlined className='site-form-item-icon' />}
							type='password'
							placeholder='Confirm Password'
						/>
					</Form.Item>

					<Form.Item shouldUpdate>
						{() => (
							<>
								<Button
									type='primary'
									htmlType='submit'
									className='login-form-button'
									style={{ marginLeft: '10px' }}
									disabled={
										!formDisabled ||
										!form.isFieldsTouched(true) ||
										!!form
											.getFieldsError()
											.filter(({ errors }) => errors.length).length ||
										form.getFieldsValue().password !==
											form.getFieldsValue().passwordConfirm
									}
								>
									Register
								</Button>
                                <Button
                                    type='primary'
                                    htmlType='button'
                                    className='login-form-button'
                                    style={{ marginLeft: '10px' }}
                                    onClick={handleCancel}
                                >
                                    Cancel
                                </Button>
							</>
						)}
					</Form.Item>
				</Form>
			</div>
		</div>
	);
};

export default App;
