import { AxiosError } from 'axios';
import { ErrorResponse } from '../../src/models/api.models';
import { useSessionHandler } from './useSessionHandler';

export const useApiHandler = () => {
    const { clearSession } = useSessionHandler();

    const handleMutation = async <TInput, TResult>(
        call: (input: TInput) => Promise<TResult>,
        input: TInput,
    ) => {
        let isError = false;
        let isSuccess = !isError;
        let message = 'Process executed successfully';
        let result;
        try {
            result = await call(input);
            return { result, isSuccess, isError, message };
        } catch (e) {
            if (e instanceof AxiosError) {
                const axiosError = e as AxiosError;
                result = axiosError.response?.data as ErrorResponse;

                if (result) {
                    if (result.code === 40103) {
                        clearSession();
                    }
                    message = result.message;
                    isError = true;
                    isSuccess = !isError;
                    return { result, isSuccess, isError, message };
                }
            }
            throw e;
        }
    };

    const handleQuery = async <TInput, TResult>(
        call: (input: TInput) => Promise<TResult>,
        input: TInput,
    ) => {
        let isError = false;
        let message = 'Process executed successfully';
        let result;
        try {
            result = await call(input);
            return { result, isError, message };
        } catch (e) {
            if (e instanceof AxiosError) {
                const axiosError = e as AxiosError;
                result = axiosError.response?.data as ErrorResponse;

                if (result) {
                    if (result.code === 40103) {
                        clearSession();
                    }
                    message = result.message;
                    isError = true;
                    return { result, isError, message };
                }
            }
            throw e;
        }
    };

    return { handleMutation, handleQuery };
};
