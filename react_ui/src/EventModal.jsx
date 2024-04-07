import { format, parseISO } from 'date-fns';

function EventModal({ events, closeModal }) {
    return (
        <div 
            className="fixed inset-0 z-50 bg-gray-600 bg-opacity-75 overflow-y-auto h-full w-full flex justify-center items-center" 
            id="my-modal"
            onClick={closeModal} // Close modal when clicking on the background
        >
            <div 
                className="relative mx-auto p-6 border w-11/12 sm:w-2/3 md:w-1/2 max-w-4xl shadow-lg rounded-md bg-white overflow-y-auto" // Added overflow-y-auto here
                style={{ maxHeight: '90vh' }} // Limit the modal's height to 90% of the viewport height
                onClick={(e) => e.stopPropagation()} // Prevent clicks from closing the modal
            >
                <div className="text-center">
                    <h3 className="text-xl md:text-2xl leading-6 font-medium text-gray-900 mb-4">Event Details</h3>
                    <div className="space-y-5">
                        {events.map((event, index) => (
                            <div key={index} className="bg-gray-50 p-4 rounded-lg shadow border">
                                <h4 className="text-lg md:text-xl font-semibold text-gray-800 mb-3">{event.summary}</h4>
                                <div className="text-gray-600 text-sm md:text-base pl-4">
                                    <p><strong>Description:</strong> {event.description}</p>
                                    {event.eventType === 'SEX' && (
                                        <>
                                            <p><strong>Duration:</strong> {event.duration} minutes</p>
                                            <p><strong>Location:</strong> {event.location}</p>
                                            <p><strong>Rating User A:</strong> {event.ratingUserA}</p>
                                            <p><strong>Rating User B:</strong> {event.ratingUserB}</p>
                                        </>
                                    )}
                                    {event.eventType === 'DATE' && (
                                        <>
                                            <p><strong>Date Location:</strong> {event.dateLocation}</p>
                                            <p><strong>Time:</strong> {format(parseISO(event.time), 'p')}</p>
                                        </>
                                    )}
                                 </div>
                            </div>
                        ))}
                    </div>
                    <div className="mt-6">
                        <button 
                            id="ok-btn" 
                            className="px-6 py-2 bg-indigo-500 text-white text-sm md:text-base font-medium rounded-md w-full shadow-sm hover:bg-indigo-600 focus:outline-none focus:ring-2 focus:ring-indigo-300 transition duration-150" 
                            onClick={closeModal}
                        >
                            Close
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default EventModal;
