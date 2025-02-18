import { Alert, Layout, Space } from "antd";
import Navbar from "../../../src/components/Navbar/Navbar";
import useNotificationHandler from "../../hooks/useNotificationHandler";
import { useSessionHandler } from "../../hooks/useSessionHandler";
import { Content, Header } from "antd/es/layout/layout";
import { Outlet } from "react-router-dom";
import mountain from "../../../src/assets/plane3.gif";


const Main = () => {

    const { notification, clearNotification } = useNotificationHandler();
    const { sessionContext } = useSessionHandler();

    const onClose = () => {
        console.log('I was closed.');
        clearNotification();
    };


    return (
        <>

            <Layout style={{ minHeight: '100vh', top: '50%' }}>
                {sessionContext != null ? (
                     <Header>
                        <Navbar />
                    </Header>         
                ) : null}
                    <Content className="fullContent" style={{ 
                    // Condicional para eliminar la imagen de fondo cuando se inicia sesión
			        backgroundImage: sessionContext == null ? `url(${mountain})` : 'none', 
			        backgroundRepeat: 'no-repeat',
			        backgroundSize: 'cover',
			    }}>
                    <Space direction='vertical' style={{ width: '100%' }}>

                        {notification?.message != null ? (
                            <Alert
                            style={{
                                textAlign: 'left', // Cambiado a 'left' para mantener el texto alineado a la izquierda
                                direction: 'rtl', // Cambiado a 'rtl' para mantener el texto alineado a la derecha
                                position: 'fixed',
                                width: '25%',
                                zIndex: '100',
                                top: 0, // Ajustado a la parte superior
                                right: 0, // Ajustado al borde derecho
                            }}
                                closable
                                description={notification?.message}
                                type={notification?.type}
                                showIcon
                                onClose={onClose}
                            />
                        ) : null}

                    </Space> 
                    <Outlet></Outlet>
                </Content>
            </Layout>

        </>

    );
}

export default Main;
