import React, { useState, useContext } from "react";
import axios from "../API/axios.jsx";
import { useNavigate } from "react-router-dom";
import AuthContext from "../Context/AuthProvider";
import UserContext from "../Context/UserProvider";

const AUTH_URL = '/auth/authenticate'

function SignInForm() {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { setAuth, persist, setPersist } = useContext(AuthContext);
  const { fetchUser } = useContext(UserContext);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const togglePersist = () => {
    setPersist(prev => !prev);
  }

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post(
        AUTH_URL,
        formData,
        { headers: { 'Content-Type': 'application/json' } }
      );
      persist && localStorage.setItem("jwtToken", response.data.accessToken);
      persist && localStorage.setItem("persist", persist)
      setAuth(response.data.accessToken)

      //spaghetti pesto alla genovese
      const fetchResponse = await fetchUser(response.data.accessToken);
      navigate(`/${fetchResponse.role.toLowerCase()}/home`);

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
    <div className="login-container">
      <h1>Log In</h1>
      {error == "" ? <></> : <p style={{ color: "red" }}>{error}</p>}
      <form onSubmit={handleSubmit}>
        <div className="inputs-container">
          <div className="login-inputs-container">
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
            />
          </div>
          <div className="login-inputs-container">
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
          </div>
          <div className="login-check-container">
            <input
              type="checkbox"
              id="persist"
              onChange={togglePersist}
              checked={persist}
            />
            <label htmlFor="persist">Remember Me</label>
          </div>
        </div>
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
