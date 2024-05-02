//this is currently not in use, will either delete or repurpose

import React from 'react';
import axios from "../API/axios.jsx";

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
    <div>
      <button onClick={handleLogout}>Log out</button>
    </div>
  );
}

export default LogoutComponent;