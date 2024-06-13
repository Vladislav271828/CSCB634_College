import axios from '../API/axios';
import AuthContext from '../Context/AuthProvider';
import UserContext from '../Context/UserProvider';
import { useContext } from 'react';

const REFRESH_URL = '/auth/refresh'

const useRefreshToken = () => {

    const { auth, setAuth, persist } = useContext(AuthContext);
    const { fetchUser } = useContext(UserContext);

    const refresh = async () => {
        var token = auth;
        if (!auth) token = localStorage.getItem("jwtToken");
        try {
            const response = await axios.post(REFRESH_URL, {}, {
                headers: { "Authorization": `Bearer ${token}` },
                withCredentials: true
            });
            token = response.data.accessToken;
            console.log(token)
            await fetchUser(response.data.accessToken);
            persist && localStorage.setItem("jwtToken", response.data.accessToken);
            setAuth(response.data.accessToken);
            return response.data.accessToken;
        }
        catch (error) {
            if (error.response?.status === 400) {
                setAuth(token);
                await fetchUser(token);
                return token;
            }
            console.log(error.response?.data.message);
            localStorage.removeItem("jwtToken");
            localStorage.removeItem("persist");

            return "";
        }
    }
    return refresh;
};

export default useRefreshToken;