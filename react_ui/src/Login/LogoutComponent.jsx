import React from 'react';
import axios from "../API/axios.jsx";

function LogoutComponent() {
  const handleLogout = async () => {
    try {
      //logout request goes here... IF I HAD ONE!!

      // await axios.post('/auth/logout', {
      //   headers: {
      //     Authorization: `Bearer ${localStorage.getItem('jwtToken')}`
      //   }
      // });
      localStorage.removeItem('jwtToken');
      window.location.href = '/login';
    } catch (error) {
      console.error('Logout failed:', error);
    }
  };

  return (
    <div>
      <button className='logout-btn' onClick={handleLogout}>Log out</button>
    </div>
  );
}

export default LogoutComponent;