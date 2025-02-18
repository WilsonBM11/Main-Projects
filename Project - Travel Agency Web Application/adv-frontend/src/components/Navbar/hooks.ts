import { useNavigate } from "react-router-dom";
import { useSessionHandler } from "../../hooks/useSessionHandler";

const useDependencies = () => {

	const {clearSession} = useSessionHandler();
    const navigate = useNavigate();

	const handleLogOut = () => {
        clearSession();
        navigate("/");
       
    };

	return {handleLogOut};
};

export default useDependencies;