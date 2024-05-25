import { createContext, useState, useContext } from "react";
import AuthContext from "./AuthProvider";
import axios from "../API/axios";

const UserContext = createContext({});
const FETCH_USER_URL = '/auth/getUserDetails'

export const UserProvider = ({ children }) => {
    // const [firstName, setFirstName] = useState("");
    // const [lastName, setLastName] = useState("");
    // const [email, setEmail] = useState("");
    const [role, setRole] = useState("");

    const [errMsg, setErrMsg] = useState('');

    const { auth } = useContext(AuthContext);

    const fetchUser = async (overwrite = "") => {
        if (!overwrite) overwrite = auth
        try {
            const response = await axios.get(FETCH_USER_URL, {
                headers: { "Authorization": `Bearer ${overwrite}` }
            });
            // setFirstName(response.data.firstName);
            // setLastName(response.data.lastName);
            // setEmail(response.data.email);
            setRole(response.data.role);
            return response.data;
        } catch (err) {
            if (!err?.response) {
                setErrMsg('Unable to connect to server.');
            }
            else if (err.response?.status === 401) {
                //refresh goes through but the token is invalid... why
                throw err;
            }
            else {
                setErrMsg(err.response.data.message);
            }
        }
    }

    return (
        <UserContext.Provider value={{ fetchUser, setErrMsg, errMsg, role }}>
            {children}
        </UserContext.Provider>
    )
}

export default UserContext;