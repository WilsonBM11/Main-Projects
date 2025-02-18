import { Alert } from "antd";
import React, { useState } from 'react';

export const useAlerts = () => {
    const [alert, setAlert] = useState<JSX.Element | null>(null);
    const [alertClosed, setAlertClosed] = useState<Promise<void>>(Promise.resolve());

    const successAlert = (message: string | null) => {
        let resolveClosure: () => void;
        const alertClosure = new Promise<void>((resolve) => {
            resolveClosure = resolve;
        });

        setAlertClosed(alertClosure);

        setAlert(
            <Alert
                className="success"
                message="Success"
                description={message}
                type="success"
                showIcon
                closable
                afterClose={() => {
                    setAlert(null);
                    resolveClosure();
                }}
            />
        );
    };

    const errorAlert = (message: string | null) => {
        setAlert(
            <Alert
                className="error"
                message="Error"
                description='Request failed with status code 500'
                type="error"
                showIcon
                closable
                afterClose={() => setAlert(null)}
            />
        );
    };

    return { alert, successAlert, errorAlert, alertClosed };
};

export default useAlerts;
