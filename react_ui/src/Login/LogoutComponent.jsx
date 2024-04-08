import React from 'react';
import axios from "../API/axios.jsx";
import { FiLogOut } from 'react-icons/fi';

function LogoutComponent() {
  const handleLogout = async () => {
    try {
      await axios.post('/auth/register', {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
        }
      });
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