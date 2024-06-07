import { Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute from './Login/PrivateRoute';
import PersistLogin from './Login/PersistLogin'

import SignUpForm from './Login/SignUpForm';
import HomePage from './BaseComponents/Dashboard';
import Login from './Login/Login';
import Header from './BaseComponents/Header';
import Footer from './BaseComponents/Footer';
import FourOhFour from './BaseComponents/404';
import { useContext } from 'react';
import UserContext from './Context/UserProvider';
import Form from './BaseComponents/Form';

import structures from "./API/structures"

function App() {

  const { role } = useContext(UserContext);

  return (
    <>
      <Header />
      <div className='app-content'>

        <Routes>
          <Route path="/signup" element={<SignUpForm />} />
          <Route path="/login" element={<Login />} />

          <Route element={<PersistLogin />}>

            <Route element={<PrivateRoute allowedRoles={["ADMIN"]} />}>
              <Route path="/admin/dashboard" element={<HomePage title="Admin" dashStructure={structures.adminDash} />} />

              <Route path="/admin/register-user" element={<Form
                title="Register a new user."
                requestURL="/auth/admin/register-user"
                successMsg="User registered successfully."
                formStructure={structures.registerUser} />} />

              <Route path="/admin/" element={<Navigate to="/admin/dashboard" replace />} />
            </Route>

            <Route path="/" element={<Navigate to={role ? `/${role.toLowerCase()}/dashboard` : "/login"} replace />} />

          </Route>

          <Route path="*" element={<FourOhFour />} />
        </Routes>

      </div>
      <Footer />
    </>
  );
}

export default App;
