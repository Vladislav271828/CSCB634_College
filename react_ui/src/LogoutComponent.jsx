import React from 'react';
import axios from 'axios';
import { FiLogOut} from 'react-icons/fi';

function LogoutComponent() {
  const handleLogout = async () => {
    try {
      const axiosInstance = axios.create({
        baseURL: 'http://localhost:8080',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
        }
      });
      
      await axiosInstance.post('/api/v1/auth/logout');
      localStorage.removeItem('jwtToken'); // Remove the token from local storage
      window.location.href = '/login'; // Redirect to homepage or login page
    } catch (error) {
      console.error('Logout failed:', error);
      // Optionally handle errors, such as displaying a message to the user
    }
  };

  return (
    <div className="w-full flex justify-center">
      <FiLogOut size={24} onClick={handleLogout} className="cursor-pointer hover:text-gray-400 text-white" />
    </div>
  );
}

export default LogoutComponent;