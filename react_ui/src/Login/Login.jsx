import React, { useState } from "react";
import axios from "../API/axios.jsx";
import { useNavigate } from "react-router-dom";

function SignInForm() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate(); // Hook for navigation

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        "/auth/authenticate",
        formData,
        { headers: { 'Content-Type': 'application/json' } }
      );
      localStorage.setItem("jwtToken", response.data.token);
      navigate("/home");
    } catch (err) {
      if (!err?.response) {
        setError('Unable to connect to server.');
      }
      if (err.response.data.message.includes("User is not verified"))
        setError(err.response.data.message);
      else {
        setError('Invalid email or password.');
      }
    }
  };

  const handleSignUpRedirect = () => {
    navigate("/signup");
  };

  return (
    <div>
      <h1 className="login-form-header">Log In</h1>
      {error == "" ? <></> : <p className='login-form-text' style={{ color: "red" }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <label>Email:</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={(e) => {
            handleChange(e);
            setError("");
          }}
          required
        // className={`shadow appearance-none border ${inputError ? "bg-red-100" : "bg-white"
        //   } rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline`}
        />
        <label>Password:</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={(e) => {
            handleChange(e);
            setError("");
          }}
          required
        />
        <div>
          <button type="submit">Sign In</button>
          <button
            type="button"
            onClick={handleSignUpRedirect}
          > Register (debug)
          </button>
        </div>
      </form>
    </div>
  );
}

export default SignInForm;
