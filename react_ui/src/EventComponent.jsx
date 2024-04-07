import React, { useState, useEffect } from 'react';
import axios from 'axios';
import EventForm from './EventForm';

function EventComponent({ eventNotification }) {
  const [eventType, setEventType] = useState('SEX');
  const [formData, setFormData] = useState({
    description: '', summary: '', location: '', ratingUserA: '', ratingUserB: '', dateSummary: '', dateLocation: '', duration: '', time: '',
  });
  const [createdEventResponse, setCreatedEventResponse] = useState(null);
  const [isSubmitted, setIsSubmitted] = useState(false);
  const [formVisible, setFormVisible] = useState(true);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    headers: { Authorization: `Bearer ${localStorage.getItem('jwtToken')}` },
  });

  useEffect(() => {
    if (eventNotification && !Array.isArray(eventNotification)) {
      console.log("Event updates received:", eventNotification);
      setCreatedEventResponse(eventNotification);
      setIsSubmitted(true);
    }
  }, [eventNotification]);

  const handleInputChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const payload = {
        ...formData,
        eventType: eventType,
        time: eventType === 'DATE' ? formData.time : null,
        ratingUserA: eventType !== 'DATE' ? parseFloat(formData.ratingUserA) : null,
        ratingUserB: eventType !== 'DATE' ? parseFloat(formData.ratingUserB) : null,
      };
      const response = await axiosInstance.post('/api/v1/event/create', payload);
      setCreatedEventResponse(response.data);
      setIsSubmitted(true);
      setFormVisible(true); 
      setIsModalOpen(true); // Open the modal on smaller screens

      setFormData({
        description: '', summary: '', location: '', ratingUserA: '', ratingUserB: '', dateSummary: '', dateLocation: '', duration: '', time: '',
      });

    } catch (error) {
      console.error('Failed to log event:', error);
    }
  };

  const toggleFormVisibility = (type) => {
    // Prevent hiding the form if the same button is clicked
    if (eventType !== type) {
      setEventType(type);
      setIsSubmitted(false);
      setFormVisible(true);
    }
  };

  return (
    <div className="max-w-xl mx-auto p-4 bg-gray-50 rounded-lg shadow" style={{ height: '550px' }}>
      <div className="flex justify-between mb-4">
        <button onClick={() => toggleFormVisibility('DATE')} className={`px-4 py-2 rounded transition duration-150 ${eventType === 'DATE' ? 'bg-blue-700 text-white' : 'bg-blue-500 text-white hover:bg-blue-700'}`}>
          Log Date
        </button>
        <button onClick={() => toggleFormVisibility('SEX')} className={`px-4 py-2 rounded transition duration-150 ${eventType === 'SEX' ? 'bg-green-700 text-white' : 'bg-green-500 text-white hover:bg-green-700'}`}>
          Log Session
        </button>
      </div>

      {formVisible && (
        <EventForm
          eventType={eventType}
          formData={formData}
          handleInputChange={handleInputChange}
          handleSubmit={handleSubmit}
          isSubmitted={isSubmitted}
          eventResponse={createdEventResponse}
        />
      )}

{
  createdEventResponse && (
    <>
    {/* Overlay for smaller screens */}
    {isModalOpen && (
      <div className="sm:hidden fixed inset-0 bg-black bg-opacity-50 z-40 flex justify-center items-center" onClick={() => setIsModalOpen(false)}>
        <div className="bg-white p-4 rounded-lg shadow-lg z-50 overflow-auto max-h-full" onClick={e => e.stopPropagation()}>
          {Object.entries(createdEventResponse).filter(([key, value]) => value !== null).map(([key, value]) => (
            <div key={key} className="flex items-center space-x-2">
              <p className="font-bold text-gray-700">{key}:</p>
              <p className="text-gray-700 text-lg">{typeof value === 'object' ? JSON.stringify(value, null, 2) : value.toString()}</p>
            </div>
          ))}
        </div>
      </div>
    )}
  
    {/* Regular display for larger screens */}
    <div className="hidden sm:block mt-4 p-4 bg-white rounded-lg shadow-lg">
      {Object.entries(createdEventResponse).filter(([key, value]) => value !== null).map(([key, value]) => (
        <div key={key} className="flex items-center space-x-2">
          <p className="font-bold text-gray-700">{key}:</p>
          <p className="text-gray-700 text-lg">{typeof value === 'object' ? JSON.stringify(value, null, 2) : value.toString()}</p>
        </div>
      ))}
    </div>
  </>
  )
}

    </div>
  );
}

export default EventComponent;