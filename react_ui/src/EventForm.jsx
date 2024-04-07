import React from "react";

function EventForm({ eventType, formData, handleInputChange, handleSubmit }) {
  return (
    <div
      className="bg-white p-6 rounded-lg shadow-lg border border-gray-200 overflow-y-auto flex flex-col"
      style={{ height: "calc(100% - 48px)" }}
    >
      <form
        onSubmit={handleSubmit}
        className="flex flex-col space-y-4 flex-grow"
      >
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Description
          </label>
          <input
            type="text"
            name="description"
            value={formData.description}
            onChange={handleInputChange}
            className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            placeholder="Description"
          />
        </div>
        <div>
          <label className="block text-gray-700 text-sm font-bold mb-2">
            Summary
          </label>
          <input
            type="text"
            name="summary"
            value={formData.summary}
            onChange={handleInputChange}
            className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
            placeholder="Summary"
          />
        </div>
        {eventType === "DATE" && (
          <>
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Date Location
              </label>
              <input
                type="text"
                name="dateLocation"
                value={formData.dateLocation}
                onChange={handleInputChange}
                className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Location"
              />
            </div>

            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Time
              </label>
              <input
                type="datetime-local"
                name="time"
                value={formData.time}
                onChange={handleInputChange}
                className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
              />
            </div>
          </>
        )}
        {eventType === "SEX" && (
          <>
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Rating User A
              </label>
              <input
                type="number"
                name="ratingUserA"
                value={formData.ratingUserA}
                onChange={handleInputChange}
                className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Rating from User A"
              />
            </div>
            <div>
              <label className="block text-gray-700 text-sm font-bold mb-2">
                Rating User B
              </label>
              <input
                type="number"
                name="ratingUserB"
                value={formData.ratingUserB}
                onChange={handleInputChange}
                className="appearance-none border border-gray-300 rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline"
                placeholder="Rating from User B"
              />
            </div>
          </>
        )}
        {/* Empty div as spacer */}
        <div className="flex-grow"></div>

        <button
          type="submit"
          className="w-full px-4 py-2 bg-gray-900 text-white rounded hover:bg-gray-700 transition duration-150 self-end"
        >
          Submit
        </button>
      </form>
    </div>
  );
}

export default EventForm;
