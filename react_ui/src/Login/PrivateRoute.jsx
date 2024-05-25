import { useLocation, Navigate, Outlet } from "react-router-dom";
import AuthContext from "../Context/AuthProvider";
import UserContext from "../Context/UserProvider";
import { useContext } from "react";

const PrivateRoute = ({ allowedRoles }) => {
  const { auth } = useContext(AuthContext);
  const { role } = useContext(UserContext);
  const location = useLocation();

  return (
    allowedRoles.includes(role)
      ? <Outlet />
      : auth
        ? <Navigate to="/unauthorized" state={{ from: location }} replace />
        : <Navigate to="/login" state={{ from: location }} replace />
  );
}

export default PrivateRoute;