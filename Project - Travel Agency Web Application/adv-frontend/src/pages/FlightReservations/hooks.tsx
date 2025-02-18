import { useApiHandler } from '../../hooks/useApiHandler';
import { userReservations, cancelReservation} from '../../services/flights.service';

const useDependencies = () => {
	const { handleQuery,handleMutation } = useApiHandler();

	const handleUserReservation = async () => {
		const { isError, message, result } = await handleQuery(
			userReservations,
			''
		);
		if (!isError) {
			console.log('Search successful');
			return result;
		} else {
			return null;
		}
	};
	const handleCancelReservation = async (seatId: string) => {
		const cancelRequest: CancelFlightRequest = {
			seatId: seatId,
		};
		const { isError, message, result } = await handleMutation(
			cancelReservation,
			cancelRequest
		);
		if (!isError) {
			console.log('Reservation canceled');
			return result;
		} else {
			return null;
		}
	}

	return { handleUserReservation, handleCancelReservation };
};

export default useDependencies;
