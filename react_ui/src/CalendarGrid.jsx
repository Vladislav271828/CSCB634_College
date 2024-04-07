import React, { useState } from 'react';
import { format, startOfMonth, endOfMonth, eachDayOfInterval, addMonths, subMonths, parseISO, getDay } from 'date-fns';
import EventModal from './EventModal'
function CalendarGrid({ events }) {
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedDayEvents, setSelectedDayEvents] = useState([]);
  const [showModal, setShowModal] = useState(false);

  const startDayOfMonth = startOfMonth(currentMonth);
  const endDayOfMonth = endOfMonth(currentMonth);

  // Find the index of the first day of the month
  const startDayIndex = getDay(startDayOfMonth);

  // Generate days for the current month
  const daysInMonth = eachDayOfInterval({
    start: startDayOfMonth,
    end: endDayOfMonth,
  });

  // Create placeholders for days before the first day of the month
  const placeholderDays = Array.from({ length: startDayIndex }, (_, index) => (
    <div key={`placeholder-${index}`} className="border-0 p-2 rounded-lg bg-transparent"></div>
  ));

  // Handle day click to show events of that day
  const handleDayClick = (day) => {
    const dayEvents = events.filter(event => {
      const eventDate = event.eventType === 'DATE' ? event.time : event.created;
      return format(parseISO(eventDate), 'yyyy-MM-dd') === format(day, 'yyyy-MM-dd');
    });
    setSelectedDayEvents(dayEvents);
    setShowModal(true);
  };

  // Count events for each day in the current month
  const countEventsForDay = (day) => {
    const dayEvents = events.filter(event => {
      const eventDate = event.eventType === 'DATE' ? event.time : event.created;
      // Check if eventDate is not null or undefined before parsing
      if (!eventDate) {
        console.error('Event date is null or undefined for event:', event);
        return false; // Skip this event
      }
      return format(parseISO(eventDate), 'yyyy-MM-dd') === format(day, 'yyyy-MM-dd');
    });
  
    const count = { SEX: 0, DATE: 0 };
    dayEvents.forEach(event => count[event.eventType]++);
    return count;
  };
  

  return (
    <div className="flex justify-center">
    <div className="mt-8 mb-16 p-6 bg-white rounded-xl shadow-xl max-w-4xl w-full">
      <div className="flex justify-between items-center mb-5">
        <button onClick={() => setCurrentMonth(addMonths(currentMonth, -1))} className="text-gray-600 hover:text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
        </button>
        <span className="text-lg font-semibold text-gray-800">{format(currentMonth, 'MMMM yyyy')}</span>
        <button onClick={() => setCurrentMonth(addMonths(currentMonth, 1))} className="text-gray-600 hover:text-gray-700">
          <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
          </svg>
        </button>
      </div>
      <div className="grid grid-cols-7 gap-4 text-sm font-medium text-left text-gray-500 border-b mb-2">
        {['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'].map((day, index) => (
          <div key={index}>{day}</div>
        ))}
      </div>
      <div className="grid grid-cols-7 gap-0 ">
      {placeholderDays}
      {daysInMonth.map(day => {
        const { SEX, DATE } = countEventsForDay(day);
        const isToday = format(day, 'yyyy-MM-dd') === format(new Date(), 'yyyy-MM-dd');
        return (
          <div key={day.toISOString()} onClick={() => handleDayClick(day)}
            className={`relative border-0 p-3 rounded-lg cursor-pointer ${isToday ? 'bg-blue-100' : 'hover:bg-gray-100'}`}>
            <span className={`text-sm ${isToday ? 'text-blue-600 font-bold' : ''}`}>{format(day, 'dd')}</span>
            <div className="absolute top-1 right-1 flex space-x-1">
              {SEX > 0 && <div className="w-2 h-2 bg-rose-500 rounded-full"></div>}
              
              {DATE > 0 && <div className="w-2 h-2 bg-blue-300 rounded-full"></div>}
            </div>
           
          </div>
        );
      })}
    </div>
  </div>
  {showModal && <EventModal events={selectedDayEvents} closeModal={() => setShowModal(false)} />}
</div>
  
  );
}

export default CalendarGrid;
