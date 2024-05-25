//page to help with debugging, will be discarded later
//i am leaving the useless comments in just so you can admire them
//🦐 chatgpt

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
      navigate('/login');
      // Handle success here
    } catch (err) {
      const message = { other: err.response?.data.message }
      setErrors(message)
    }
  };

  // Function to handle redirection to the Sign In page
  const handleSignInRedirect = () => {
    navigate('/login'); // Redirect to the sign-in page
  };

  return (
    <div className='login-container'>
      <div>This page is meant for registering the first admin. It will not work if there is already at least one user. I can't be bothered to fix any bugs in this page as it is only temporary and will be removed. I hate React.</div>
      {errors.firstName && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.firstName}</div>}
      {errors.email && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.email}</div>}
      {errors.password && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.password}</div>}
      {errors.other && <div style={{ color: 'red', fontStyle: 'italic' }}>{errors.other}</div>}
      <form onSubmit={handleSubmit}>
        <div className="inputs-container">
          <div className="login-inputs-container">
            <label>First Name:</label>
            <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} />
          </div>
          <div className="login-inputs-container">
            <label>Last Name:</label>
            <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} />
          </div>
          <div className="login-inputs-container">
            <label>Email:</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} />
          </div>
          <div className="login-inputs-container">
            <label>Password:</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} />
          </div>
          <div>
            <button type="submit">Sign Up</button>
            <button type="button" onClick={handleSignInRedirect} style={{ marginLeft: '10px' }}>Go back</button>
          </div>
        </div>
      </form>
    </div>

  );
}

export default SignUpForm;