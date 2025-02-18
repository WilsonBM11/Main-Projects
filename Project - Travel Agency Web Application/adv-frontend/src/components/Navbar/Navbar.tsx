import {
	SearchOutlined,
	FileSearchOutlined,
	AppstoreAddOutlined,
	LogoutOutlined,
	HomeOutlined,
} from '@ant-design/icons';
import type { MenuProps } from 'antd';
import { Menu } from 'antd';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import { useSessionHandler } from '../../hooks/useSessionHandler';
import useDependencies from './hooks';

import './navbarStyles.css';

const items: MenuProps['items'] = [
	{
		label: <Link to='/'>Home</Link>,
		icon: <HomeOutlined />,
		key: '0',
	},
	{
		label: <Link to='/hotels'>Search Hotel</Link>,
		icon: <FileSearchOutlined />,
		key: '1',
	},
	{
		label: <Link to='/flights'>Search Flight</Link>,
		icon: <SearchOutlined />,
		key: '2',
	},
	{
		label: <Link to='/reservations'>Manage Flight Reservations</Link>,
		icon: <SearchOutlined />,
		key: '3',
	},
	{
		label: <Link to={'/hotels/reservations'}>Manage Hotel Reservations</Link>,
		icon: <SearchOutlined />,
		key: '4',
	},
	{
		label: 'Log out',
		icon: <LogoutOutlined />,
		key: '5',
	},
];

const itemsAdmin: MenuProps['items'] = [
	{
		label: <Link to='/'>Home</Link>,
		icon: <HomeOutlined />,
		key: '0',
	},
	{
		label: <Link to='/hotels'>Search Hotel</Link>,
		icon: <SearchOutlined />,
		key: '1',
	},
	{
		label: <Link to='/flights'>Search Flight</Link>,
		icon: <SearchOutlined />,
		key: '2',
	},
	{
		label: <Link to='/registerFlight'>Register Flight</Link>,
		icon: <AppstoreAddOutlined />,
		key: '3',
	},
	{
		label: <Link to='/registerHotel'>Register Hotel</Link>,
		icon: <AppstoreAddOutlined />,
		key: '4',
	},
	{
		label: <Link to='/registerRoom'>Register Room</Link>,
		icon: <AppstoreAddOutlined />,
		key: '5',
	},
	{
		label: 'Log out',
		icon: <LogoutOutlined />,
		key: '6',
	},
];

const Navbar = () => {
	const { handleLogOut } = useDependencies();

	const [current, setCurrent] = useState('');

	const onClick: MenuProps['onClick'] = e => {
		console.log('click ', e);

		setCurrent(e.key);

		if (sessionContext?.roles == 'ADMIN' && e.key == '6') {
			handleLogOut();
		}
		if (sessionContext?.roles != 'ADMIN' && e.key == '5') {
			handleLogOut();
		}
	};

	const { sessionContext } = useSessionHandler();
	return (
		<div>
			{sessionContext?.roles == 'ADMIN' ? (
				<Menu
					onClick={onClick}
					items={itemsAdmin}
					mode='horizontal'
					selectedKeys={[current]}
					className='navbar'
					theme='light'
				></Menu>
			) : (
				<Menu
					onClick={onClick}
					selectedKeys={[current]}
					mode='horizontal'
					items={items}
					className='navbar'
				></Menu>
			)}
		</div>
	);
};

export default Navbar;
