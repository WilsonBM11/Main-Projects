// src/components/FlightDetails.tsx

import { Card, Descriptions, Typography, Row, Col, Statistic } from 'antd';
import useDependencies from './hooks';
import { useEffect } from 'react';
import { FlightId } from './types';
import { useParams } from 'react-router-dom';
import { useSessionHandler } from '../../hooks/useSessionHandler';

const FlightDetails = () => {
    
    const { flightId } = useParams<string>();

    const { Title } = Typography;

    const {flightData, handleFlightDetailsRequest} = useDependencies();

    useEffect(() => {
        handleFlightDetailsRequest(flightId as string);
    },[]);

    if (!flightData) {
        return <div>Loading...</div>;
    }

    return (
        <Card>
        <Title level={2}>Flight Details</Title>
        <Descriptions bordered>
            <Descriptions.Item label="Airline">{flightData.airline}</Descriptions.Item>
            <Descriptions.Item label="Departure">{flightData.departure}</Descriptions.Item>
            <Descriptions.Item label="Destination">{flightData.destination}</Descriptions.Item>
            <Descriptions.Item label="Departure Time">{flightData.departureTime}</Descriptions.Item>
            <Descriptions.Item label="Arrival Time">{flightData.arrivalTime}</Descriptions.Item>
        </Descriptions>

        <Row gutter={16} style={{ marginTop: 20 }}>
            <Col span={8}>
            <Card>
                <Statistic
                title="First Class Price"
                value={flightData.firstClassPrice}
                prefix="$"
                precision={2}
                />
                <Statistic
                title="Seats Available"
                value={flightData.availableFirstClassSeats}
                style={{ marginTop: 20 }}
                />
            </Card>
            </Col>
            <Col span={8}>
            <Card>
                <Statistic
                title="Business Class Price"
                value={flightData.businessClassPrice}
                prefix="$"
                precision={2}
                />
                <Statistic
                title="Seats Available"
                value={flightData.availableBusinessClassSeats}
                style={{ marginTop: 20 }}
                />
            </Card>
            </Col>
            <Col span={8}>
            <Card>
                <Statistic
                title="Commercial Class Price"
                value={flightData.commercialClassPrice}
                prefix="$"
                precision={2}
                />
                <Statistic
                title="Seats Available"
                value={flightData.availableCommercialClassSeats}
                style={{ marginTop: 20 }}
                />
            </Card>
            </Col>
        </Row>
        </Card>
    );
}

export default FlightDetails
