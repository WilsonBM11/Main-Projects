import { Card, Form, InputNumber, Button, Popover, Popconfirm, PopconfirmProps, message, Spin } from 'antd';
import useDependencies from './hooks';
import { useEffect, useState } from 'react';
import "./styles.css"
import { FlightDetails, FlightEditForm, flightEditProps } from "./types";
import React from 'react';
import { EditOutlined } from '@ant-design/icons';



const FlightEdit = (Props: flightEditProps) => {

    const [form] = Form.useForm();

    const [flightData, setFlightData] = useState<FlightDetails | null>(null);

    const [readyFirstSeats, setReadyFirstSeats] = useState<boolean>(true);
    const [readyFirstPrice, setReadyFirstPrice] = useState<boolean>(true);

    const [readyBusinessSeats, setReadyBusinessSeats] = useState<boolean>(true);
    const [readyBusinessPrice, setReadyBusinessPrice] = useState<boolean>(true);

    const [readyCommercialSeats, setReadyCommercialSeats] = useState<boolean>(true);
    const [readyCommercialPrice, setReadyCommercialPrice] = useState<boolean>(true);

    const confirm: PopconfirmProps['onConfirm'] = () => {
        form.submit();
    };

    const cancel: PopconfirmProps['onCancel'] = () => {
        message.error('Canceled Flight Edit');
    };

 

    const { handleFlightDetailsRequest, handleFlightEdit } = useDependencies();

    useEffect(() => {
        const fetchData = async () => {
          try {
            form.resetFields(); // Reiniciar campos del formulario al principio
            const flightResult = await handleFlightDetailsRequest(Props.id);
            setFlightData(flightResult); // Actualizar flightData después de obtener los detalles del vuelo
          } catch (error) {
            console.error('Error fetching flight details:', error);
          }
        };
    
        fetchData(); // Llamar a la función para obtener y configurar los datos del vuelo
      }, [Props.id, form]); // Dependencias del useEffect
    
      useEffect(() => {
        if (flightData) {
          const initialValues = {
            availableFirstClassSeats: flightData.availableFirstClassSeats,
            firstClassPrice: flightData.firstClassPrice,
            availableBusinessClassSeats: flightData.availableBusinessClassSeats,
            businessClassPrice: flightData.businessClassPrice,
            availableCommercialClassSeats: flightData.availableCommercialClassSeats,
            commercialClassPrice: flightData.commercialClassPrice,
          };
          form.setFieldsValue(initialValues); // Establecer los valores iniciales del formulario
        }
      }, [flightData, form]); // Dependencia de flightData y form

    
    if (!flightData) {
        return <div>
            <Spin>
            </Spin></div>;
    }

    return (
        <Card>
            <Form onFinish={(e:FlightEditForm) => {
                e.id = flightData.id;
                handleFlightEdit(e,flightData)
                }} style={{ display: "flex", flexDirection: "column", gap: "10px" }} form={form} initialValues={{}}>
                <Popover title="First Class" trigger="hover" placement="leftTop">
                    <div className='childForm'>
                        <Form.Item label="Seats" name="availableFirstClassSeats" >
                            <InputNumber readOnly={readyFirstSeats}></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyFirstSeats(!readyFirstSeats)}><EditOutlined /></Button>
                        <Form.Item label="Price" name="firstClassPrice" >
                            <InputNumber prefix="$" readOnly={readyFirstPrice}></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyFirstPrice(!readyFirstPrice)}><EditOutlined /></Button>
                    </div>
                </Popover>

                <Popover title="Business Class" trigger="hover" placement="leftTop">
                    <div className='childForm'>

                        <Form.Item label="Seats" name="availableBusinessClassSeats">
                            <InputNumber readOnly={readyBusinessSeats} ></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyBusinessSeats(!readyBusinessSeats)}><EditOutlined /></Button>
                        <Form.Item label="Price" name="businessClassPrice" >
                            <InputNumber prefix="$" readOnly={readyBusinessPrice} ></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyBusinessPrice(!readyBusinessPrice)}><EditOutlined /></Button>
                    </div>
                </Popover>


                <Popover title="Commercial Class" trigger="hover" placement="leftTop">
                    <div className='childForm'>

                        <Form.Item label="Seats" name="availableCommercialClassSeats" >
                            <InputNumber readOnly={readyCommercialSeats} ></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyCommercialSeats(!readyCommercialSeats)}><EditOutlined /></Button>
                        <Form.Item label="Price" name="commercialClassPrice" >
                            <InputNumber prefix="$" readOnly={readyCommercialPrice} ></InputNumber>
                        </Form.Item>
                        <Button onClick={() => setReadyCommercialPrice(!readyCommercialPrice)}><EditOutlined /></Button>
                    </div>
                </Popover>

                <Popconfirm
                    title="Edit the flight"
                    description="Are you sure to edit this flight?"
                    onConfirm={confirm}
                    onCancel={cancel}
                    okText="Yes"
                    cancelText="No">
                    <Button type='primary'>Submit</Button>
                </Popconfirm>

            </Form>
        </Card>

    );
}

export default React.memo(FlightEdit)
