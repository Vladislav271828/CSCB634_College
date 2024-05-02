import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const PrivateRoute = () => {
  const auth = localStorage.getItem('jwtToken'); // Check if the token exists
  return (
    auth
      ? <Outlet />
      : <Navigate to="/login" state={{ from: location }} replace />
  );
};

export default PrivateRoute;
