import React, { useState } from 'react';
import axios from 'axios';
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
      const response = await axios.post('http://localhost:8080/api/v1/auth/register', formData);
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
    <div className="flex h-screen bg-gray-100">
    <div className="m-auto shadow-lg rounded-lg overflow-hidden max-w-md w-full">
      <form onSubmit={handleSubmit} className="bg-white p-8">
        <div className="mb-4">
          <label className="block text-gray-700 text-lg font-bold mb-2">First Name:</label>
          <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
          {errors.firstName && <div className="text-red-500 text-xs italic">{errors.firstName}</div>}
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-lg font-bold mb-2">Last Name:</label>
          <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
        </div>
        <div className="mb-4">
          <label className="block text-gray-700 text-lg font-bold mb-2">Email:</label>
          <input type="email" name="email" value={formData.email} onChange={handleChange} className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" />
          {errors.email && <div className="text-red-500 text-xs italic">{errors.email}</div>}
        </div>
        <div className="mb-6">
          <label className="block text-gray-700 text-lg font-bold mb-2">Password:</label>
          <input type="password" name="password" value={formData.password} onChange={handleChange} className="shadow appearance-none border rounded w-full py-3 px-4 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" />
          {errors.password && <div className="text-red-500 text-xs italic">{errors.password}</div>}
        </div>
        <div className="flex items-center justify-between">
          <button type="submit" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline text-lg">Sign Up</button>
          <button type="button" onClick={handleSignInRedirect} className="inline-block align-baseline font-bold text-sm text-blue-500 hover:text-blue-800" style={{ marginLeft: '10px' }}>Sign In</button>
        </div>
      </form>
    </div>
  </div>
  
  );
}

export default SignUpForm;