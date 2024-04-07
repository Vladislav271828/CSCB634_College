import React, { useState, useEffect } from 'react';
import axios from 'axios';

const UserLinkComponent = ({currentUser, isUserLinked, onUserLinkedStatusChange, notification  }) => {
  const [email, setEmail] = useState('');
  const [requestStatus, setRequestStatus] = useState('');
  const [userIsLinked, setUserIsLinked] = useState(isUserLinked);


  const token = localStorage.getItem('jwtToken');
  const axiosConfig = { headers: { 'Authorization': `Bearer ${token}` } };

  
  useEffect(() => {
    console.log("Notification received:", notification); // Add this for debugging

    if (notification && notification.message === 'Link established') {
      if (typeof onUserLinkedStatusChange === 'function') {
        onUserLinkedStatusChange(true);
      }
    } else if (notification && notification.message.includes('new link request')) {
          setRequestStatus('Request received');
        } else if (notification && notification.message.includes('User unlinked')) {
          setUserIsLinked(false); // Trigger parent update
        } else if (notification && notification.message.includes('Link request declined')) {
          setRequestStatus('');
        } else {
          setRequestStatus(notification && notification.message);
        }
    // Handle other notification types as needed
  }, [notification, onUserLinkedStatusChange]);

  



  const sendLinkRequest = async (e) => {
    e.preventDefault(); // This line prevents the default form submission behavior

    try {
      const response = await axios.post('http://localhost:8080/api/v1/request/send-request-to-user', { email }, axiosConfig);
      setRequestStatus('Request sent');
    } catch (error) {
      console.error('Error sending link request:', error);
    }
  };

  const cancelLinkRequest = async () => {
    try {
      await axios.post(`http://localhost:8080/api/v1/request/decline-request`, {}, axiosConfig);
      setRequestStatus('');
    } catch (error) {
      console.error('Error cancelling link request:', error);
    }
  };
  
  const acceptLinkRequest = async () => {
    try {
      await axios.post('http://localhost:8080/api/v1/request/accept-request', {}, axiosConfig);
    } catch (error) {
      console.error('Error accepting link request:', error);
    }
  };
  
  const declineLinkRequest = async () => {
    try {
      await axios.post('http://localhost:8080/api/v1/request/decline-request', {}, axiosConfig);
    } catch (error) {
      console.error('Error declining link request:', error);
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
    <div className="m-auto w-full max-w-xs">
    <h1 className="text-2xl font-bold mb-4">Welcome, {localStorage.getItem("userFirstName")}!</h1>
        <p className="mb-6 text-lg">Link with a user by submitting a link request to them in the form below:</p>
      <div className="bg-white p-6 rounded-lg shadow-lg text-center">
        
        {!userIsLinked && (
          requestStatus === 'Request sent' ? (
            <div>
            <button 
              className="bg-red-500 text-white font-bold py-2 px-4 rounded hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-opacity-50 transition duration-300 w-full"
              onClick={cancelLinkRequest}
            >
              Cancel Request
            </button>
          </div>
          
            
          ) : requestStatus === 'Request received' ? (
            <div className="flex justify-between">
              <button className="bg-green-500 text-white p-2 rounded hover:bg-green-700 transition duration-300" onClick={acceptLinkRequest}>Accept</button>
              <button className="bg-red-500 text-white p-2 rounded hover:bg-red-700 transition duration-300" onClick={declineLinkRequest}>Decline</button>
            </div>
          ) : (
            <form onSubmit={sendLinkRequest} className="flex flex-col space-y-2">
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="Enter user's email"
                className="border border-gray-300 p-2 rounded focus:outline-none focus:border-blue-500"
              />
              <button type="submit" className="bg-blue-500 text-white p-2 rounded hover:bg-blue-700 transition duration-300 w-full">Send Link Request</button>
            </form>
          )
        )}
      </div>
    </div>
  </div>
  
  );
};

export default UserLinkComponent;