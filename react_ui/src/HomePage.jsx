import React, { useState, useEffect } from 'react';
import axios from 'axios';
import SockJS from 'sockjs-client';
import Stomp from 'stompjs';
import UserLinkComponent from "./UserLinkComponent";
import LogoutComponent from "./LogoutComponent";
import EventComponent from "./EventComponent";
import CalendarGrid from "./CalendarGrid";
import { FiLogOut, FiCalendar, FiFilePlus, FiMenu } from 'react-icons/fi';

function HomePage() {
  const [isUserLinked, setIsUserLinked] = useState(false);
  const [userNotification, setUserNotification] = useState(null);
  const [events, setEvents] = useState([]);
  const [eventNotification, setEventNotification] = useState(null);
  const [stompClient, setStompClient] = useState(null);
  const [currentUser, setCurrentUser] = useState(null);
  const [activeComponent, setActiveComponent] = useState('calendar'); // default to 'calendar'
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws-message');
    const client = Stomp.over(socket);
    client.connect({}, (frame) => {
      console.log('Connected: ' + frame);
      setStompClient(client);
      fetchUserDetails(client);
    });
  }, []);

  useEffect(() => {
    if (isUserLinked && stompClient) {
      subscribeToEventTopic(stompClient);
      fetchEvents();
    }
  }, [isUserLinked, stompClient]);

  const fetchUserDetails = async (client) => {
    try {
      const response = await axios.get('http://localhost:8080/api/v1/user/get-user', {
        headers: { Authorization: `Bearer ${localStorage.getItem('jwtToken')}` },
      });
      if (response.data) {
        localStorage.setItem('userId', response.data.id);
        localStorage.setItem('userFirstName', response.data.firstname);

        setCurrentUser(response.data); // Assuming 'name' is the property holding the user's name
        setIsUserLinked(!!response.data.linkedUserEmail);
        subscribeToUserTopic(client, response.data.id);
      }
    } catch (error) {
      console.error('Failed to fetch user details:', error);
    }
  };

  const subscribeToUserTopic = (client, userId) => {
    client.subscribe('/topic/user.' + userId, (message) => {
      console.log('User notification received:', message.body);
      const notification = JSON.parse(message.body);
      setUserNotification(notification);
      if (notification.linkedUserEmail) {
        handleUserLinkedStatusChange(true);
      }
    });
  };

  const subscribeToEventTopic = (client) => {
    client.subscribe('/topic/event.' + localStorage.getItem('userId'), (message) => {
      console.log('Event update received:', message.body);
      const notification = JSON.parse(message.body);
  
      if (notification === "New Event Added") {
        // Trigger re-fetch or handle the individual event
        fetchEvents(); // Example: Re-fetch events to update the state
      } else {
        // Assuming notification is an array of events here
        setEventNotification(notification); // Bulk events update
        setEvents(notification); // Update your events state
      }
    });
  };


  const fetchEvents = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/v1/event/getAll', {
        headers: { Authorization: `Bearer ${localStorage.getItem('jwtToken')}` },
      });
      if (Array.isArray(response.data)) {
        setEvents(response.data);
      } else {
        console.error('Invalid response data:', response.data);
      }
    } catch (error) {
      console.error('Failed to fetch events:', error);
    }
  };


  const handleNavClick = (componentName) => {
    if (activeComponent !== componentName) {
      setActiveComponent(componentName);
    }
  };

  const NavButton = ({ icon, componentName, active }) => {
    const bgColor = active ? 'bg-white' : 'bg-gray-900';
    const iconColor = active ? 'text-gray-900' : 'text-white';
    const buttonStyle = active ? 'border-transparent shadow-none' : 'border-t border-b border-gray-600';


    return (
      <div
        onClick={() => handleNavClick(componentName)}
        className={`w-full p-4 flex justify-center items-center cursor-pointer ${bgColor} ${buttonStyle}`}
      >
        <div className={iconColor}>{icon}</div>
      </div>
    );
  };
  
  

  return (
    <div className="flex min-h-screen bg-gray-100">
      {isUserLinked && (
        <>
          {/* Overlay when sidebar is open on small screens */}
          <div className={`fixed inset-0 bg-black bg-opacity-50 z-20 ${isSidebarOpen ? 'block' : 'hidden'} sm:hidden`} onClick={() => setIsSidebarOpen(false)}></div>
  
          {/* Sliding sidebar for smaller screens */}
          <div className={`fixed inset-y-0 left-0 transform ${isSidebarOpen ? 'translate-x-0' : '-translate-x-full'} w-16 bg-gray-900 text-white flex flex-col items-center transition-transform duration-300 ease-in-out z-30 sm:hidden`}>
            <button className="p-4" onClick={() => setIsSidebarOpen(!isSidebarOpen)}><FiMenu size={24} className="text-white" /></button>
            <NavButton icon={<FiFilePlus size={24} />} componentName="event" active={activeComponent === 'event'} />
            <NavButton icon={<FiCalendar size={24} />} componentName="calendar" active={activeComponent === 'calendar'} />
            <div className="mt-auto mb-4 w-full">
              <LogoutComponent />
            </div>
          </div>
  
          {/* Static sidebar for larger screens */}
          <div className="hidden sm:flex flex-col items-center bg-gray-900 text-white w-16 md:w-48 z-10">
            <NavButton icon={<FiFilePlus size={24} />} componentName="event" active={activeComponent === 'event'} />
            <NavButton icon={<FiCalendar size={24} />} componentName="calendar" active={activeComponent === 'calendar'} />
            <div className="mt-auto mb-4 w-full">
              <LogoutComponent />
            </div>
          </div>
        </>
      )}
      <div className={`flex-grow p-6 `}>
        <button className="sm:hidden absolute top-4 left-4 z-40 text-white" onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
          <FiMenu size={24} />
        </button>
        {!isUserLinked ? (
          <UserLinkComponent currentUser={currentUser} onUserLinkedStatusChange={setIsUserLinked} isUserLinked={isUserLinked} notification={userNotification} />
        ) : (
          <div className="max-w-4xl mx-auto">
            {activeComponent === 'event' && <EventComponent eventNotification={eventNotification} />}
            {activeComponent === 'calendar' && <CalendarGrid events={events} />}
          </div>
        )}
      </div>
    </div>
  );
  
  
};

export default HomePage;