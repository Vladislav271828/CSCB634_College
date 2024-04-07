import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css"; // Import the CSS for react-toastify

function SignInForm() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate(); // Hook for navigation
  const [shake, setShake] = useState(false);
  const [inputError, setInputError] = useState(false);

  const handleChange = (e) => {
    if (shake) setShake(false); // Reset the shake effect when the user starts typing
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Reset the shake and inputError states on every submit
    setShake(false);
    setInputError(false);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/v1/auth/authenticate",
        formData
      );
      localStorage.setItem("jwtToken", response.data.token);
      navigate("/home");
    } catch (error) {
      console.error(error);
      toast.error("Invalid username or password, please.", {
        position: "top-right",
        autoClose: 1000,
        hideProgressBar: false,
        closeOnClick: false,
        pauseOnHover: false,
        draggable: true,
        progress: undefined,
      });
      setShake(true);
      setInputError(true);
      // Remove the shake effect after it's done
      setTimeout(() => setShake(false), 1000);
    }
  };

  // Function to handle redirection to the Sign Up page
  const handleSignUpRedirect = () => {
    navigate("/signup"); // Redirect to the sign-up page
  };

  return (
    <div className="flex flex-col h-screen bg-white">
      {/* Welcoming message */}
      <div className="text-center pt-12 pb-5">
        <h1 className="text-4xl font-semibold mb-2">Welcome Back!</h1>
        <p className="text-lg">
          If you're here for the first time, we'd be very happy to have you on
          board. 
        </p>
      </div>
      <ToastContainer />
      {/* Form container with adjusted alignment */}
      <div className="flex-grow">
        <div
          className={`flex justify-center items-start pt-0 sm:pt-24 `}
        >
          <div className={`shadow-lg rounded-lg overflow-hidden w-full max-w-md ${shake ? "shake" : ""}`}>
            <form onSubmit={handleSubmit} className="bg-white p-8 mt-10 pt-12">
              <div className="mb-4">
                <label className="block text-gray-700 text-lg font-bold mb-2">
                  Email:
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={(e) => {
                    handleChange(e);
                    setInputError(false);
                  }}
                  required
                  className={`shadow appearance-none border ${
                    inputError ? "bg-red-100" : "bg-white"
                  } rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
                />
              </div>
              <div className="mb-6">
                <label className="block text-gray-700 text-lg font-bold mb-2">
                  Password:
                </label>
                <input
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={(e) => {
                    handleChange(e);
                    setInputError(false);
                  }}
                  required
                  className={`shadow appearance-none border ${
                    inputError ? "bg-red-100" : "bg-white"
                  } rounded w-full py-3 px-4 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline`}
                />
              </div>
              <div className="flex items-center justify-between">
                <button
                  type="submit"
                  className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline text-lg"
                >
                  Sign In
                </button>
                <button
                  type="button"
                  onClick={handleSignUpRedirect}
                  className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800"
                > Sign Up
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );}

export default SignInForm;
