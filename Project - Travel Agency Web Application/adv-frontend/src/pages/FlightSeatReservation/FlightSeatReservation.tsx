import { Button, DatePicker, Form, Input, Select, Space } from "antd";
import useDependencies from "./hooks"
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { Key, useEffect } from "react";
import { useParams } from "react-router-dom";
import Title from "antd/es/typography/Title";


const FlightSeatReservation = () =>{

    const { flightId } = useParams<string>();

    const {handleSubmit, rules} = useDependencies();
    
    const [form] = Form.useForm();

    useEffect(() => {
        form.setFieldsValue({ flightId });
    }, [form, flightId]);

    return (
      <>
        <Title level={2} style={{ textAlign: 'center' }}>Flight Seat Reservation</Title>
        <Form
          form={form}
          name="flight_booking"
          onFinish={handleSubmit}
          initialValues={{ companions: [] }}
          layout="vertical"
          style={{ maxWidth: 600, margin: '0 auto' }}
        >

        <Form.Item
            name="flightId"
            style={{ display: 'none' }}
        >
        <Input />
        </Form.Item>
    
          <Form.Item name="flightClass" rules={rules.flightClass} label="Class Seat">
            <Select
                placeholder="Select your class"
                style={{ width: 150 }}
                options={[
                    { value: 'FIRST', label: 'First' },
                    { value: 'BUSINESS', label: 'Business' },
                    { value: 'COMMERCIAL', label: 'Commercial' },
                ]}
            />
          </Form.Item>
    
          <Form.Item
            name="passengerName"
            label="Your Name"
            rules={rules.passengerName}
          >
            <Input/>
          </Form.Item>
    
          <Form.Item
            name="dateOfBirth"
            label="Your Date of Birth"
            rules={rules.dateOfBirth}
          >
            <DatePicker format="YYYY-MM-DD" style={{ width: '100%' }} placeholder="e.g., 2002-05-11"/>
          </Form.Item>
    
          <Form.Item
            name="passengerEmail"
            label="Your Email"
            rules={rules.passengerEmail}
          >
            <Input placeholder="e.g., example@email.com" />
          </Form.Item>
    
          <Form.Item
            name="passengerTelephone"
            label="Your Telephone"
            rules={rules.passengerTelephone}
          >
            <Input placeholder="e.g., +506 8790-5871" />
          </Form.Item>
    
          <Form.List name="companions">
            {(fields, { add, remove }) => (
              <>
                {fields.map(({ key, name, fieldKey, ...restField }) => (
                  <Space key={key} style={{ display: 'flex', marginBottom: 8 }} align="baseline">
                    <Form.Item
                      {...restField}
                      label="Companion Name"
                      name={[name, 'name']}
                      fieldKey={[fieldKey as Key, 'name']}
                      rules={rules.companionName}
                    >
                      <Input />
                    </Form.Item>
                    <Form.Item
                      {...restField}
                      label="Companion Date Of Birth"
                      name={[name, 'dateOfBirth']}
                      fieldKey={[fieldKey as Key, 'dateOfBirth']}
                      rules={rules.companionDateOfBirth}
                    >
                      <DatePicker format="YYYY-MM-DD" placeholder="e.g., 2002-03-18"/>
                    </Form.Item>
                    <MinusCircleOutlined onClick={() => remove(name)} />
                  </Space>
                ))}
                <Form.Item>
                  <Button
                    type="dashed"
                    onClick={() => add()}
                    icon={<PlusOutlined />}
                    style={{ width: '100%' }}
                  >
                    Add Companion
                  </Button>
                </Form.Item>
              </>
            )}
          </Form.List>
    
          <Form.Item>
            <Button type="primary" htmlType="submit" style={{ width: '100%' }}>
              Reserve
            </Button>
          </Form.Item>
        </Form>
        </>
      );
}

export default FlightSeatReservation