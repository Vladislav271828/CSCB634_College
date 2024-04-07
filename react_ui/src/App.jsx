import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom'; // Import Navigate
import PrivateRoute from './PrivateRoute'; // Import the PrivateRoute component

import SignUpForm from './SignUpForm';
import HomePage from './HomePage';
import Login from './Login';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/signup" element={<SignUpForm />} />
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<Navigate to="/login" />} />
          <Route element={<PrivateRoute />}>
            <Route path="/home" element={<HomePage />} />
          </Route>
        </Routes>
    </Router>
  );
}

export default App;
