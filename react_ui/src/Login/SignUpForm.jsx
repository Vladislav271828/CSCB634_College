//page to help with debugging, will be discarded later

import React, { useState } from 'react';
import axios from "../API/axios.jsx";
import { useNavigate } from 'react-router-dom';

function SignUpForm() {
  const navigate = useNavigate(); // Hook for navigation

  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
  });
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    let formIsValid = true;
    let errors = {};

    // First Name validations
    if (!formData.firstName) {
      formIsValid = false;
      errors['firstName'] = 'First name is required.';
    } else if (formData.firstName.length < 3 || formData.firstName.length > 18) {
      formIsValid = false;
      errors['firstName'] = 'First name must be between 3 and 18 characters.';
    }

    // Email validations
    if (!formData.email) {
      formIsValid = false;
      errors['email'] = 'Email is required.';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      formIsValid = false;
      errors['email'] = 'Invalid email format.';
    }

    // Password validations
    if (!formData.password) {
      formIsValid = false;
      errors['password'] = 'Password is required.';
    } else if (formData.password.length < 8) {
      formIsValid = false;
      errors['password'] = 'Password must be at least 8 characters long.';
    } else if (!/[A-Z]/.test(formData.password)) {
      formIsValid = false;
      errors['password'] = 'Password must contain an uppercase letter.';
    } else if (!/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]/.test(formData.password)) {
      formIsValid = false;
      errors['password'] = 'Password must contain a special character.';
    }

    setErrors(errors);
    return formIsValid;
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validateForm()) return;
    // API call to backend
    try {
      const response = await axios.post(
        '/auth/register',
        formData);
      localStorage.setItem('jwtToken', response.data.token); // Assuming the token is in response.data.token
      navigate('/home'); // Redirect to home page
      // Handle success here
    } catch (error) {
      console.error(error);
      // Handle error here
    }
  };

  // Function to handle redirection to the Sign In page
  const handleSignInRedirect = () => {
    navigate('/login'); // Redirect to the sign-in page
  };

  return (
    <div style={{
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      height: '100vh',
      flexDirection: 'column'
    }}>
      {errors.firstName && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.firstName}</div>}
      {errors.email && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.email}</div>}
      {errors.password && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.password}</div>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>First Name:</label>
          <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
        </div>
        <div>
          <label>Last Name:</label>
          <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
        </div>
        <div>
          <label>Email:</label>
          <input type="email" name="email" value={formData.email} onChange={handleChange} />
        </div>
        <div>
          <label>Password:</label>
          <input type="password" name="password" value={formData.password} onChange={handleChange} />
        </div>
        <div>
          <button type="submit">Sign Up</button>
          <button type="button" onClick={handleSignInRedirect} style={{ marginLeft: '10px' }}>Go back</button>
        </div>
      </form>
    </div>

  );
}

export default SignUpForm;