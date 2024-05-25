import { Outlet } from "react-router-dom";
import { useState, useEffect } from "react";
import useRefreshToken from './useRefreshToken';
import AuthContext from '../Context/AuthProvider';
import { useContext } from 'react';

const PersistLogin = () => {
    const [isLoading, setIsLoading] = useState(true);
    const refresh = useRefreshToken();
    const { auth, persist } = useContext(AuthContext);

    useEffect(() => {
        let isMounted = true;

        const verifyRefreshToken = async () => {
            try {
                await refresh();
            }
            catch (err) {
                console.error(err);
            }
            finally {
                isMounted && setIsLoading(false);
            }
        }

        !auth && persist ? verifyRefreshToken() : setIsLoading(false);
        return () => isMounted = false;
    }, [])

    return (
        <>
            {!persist
                ? <Outlet />
                : isLoading
                    ? <p style={{ margin: "10vh" }}>Loading...</p>
                    : <Outlet />
            }
        </>
    )
}

export default PersistLogin