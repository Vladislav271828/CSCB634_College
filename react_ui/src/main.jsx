import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './Context/AuthProvider';
import { UserProvider } from './Context/UserProvider.jsx';
import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <AuthProvider><UserProvider>
            <Routes>
                <Route path="*" element={<App />} />
            </Routes>
        </UserProvider></AuthProvider>
    </BrowserRouter>
)
