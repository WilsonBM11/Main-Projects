import { Alert, Button, Empty, Flex } from "antd";
import { useNavigate } from "react-router-dom";
import airplane from "../../assets/airplane.gif"

const NotFound = () => {
    const msg = "Page not found"
    const type = "error"

    const navigate = useNavigate();

    const onClose = () => {
        navigate("/");
    }
    return (
        <>
            <Empty 
            image={airplane} 
            imageStyle={{ height: 400 }} 
            description={
                <span>
                  <h2>Page not found</h2>
                </span>
              }>

                <Button type="link" onClick={onClose}>Return</Button>
            </Empty>
        </>
    );
}

export default NotFound