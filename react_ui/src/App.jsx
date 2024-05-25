import { Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute from './Login/PrivateRoute';
import PersistLogin from './Login/PersistLogin'

import SignUpForm from './Login/SignUpForm';
import HomePage from './HomePage';
import Login from './Login/Login';
import Header from './BaseComponents/Header';
import Footer from './BaseComponents/Footer';
import FourOhFour from './404';
import { useContext } from 'react';
import UserContext from './Context/UserProvider';

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
              <Route path="/admin/home" element={<HomePage />} />
              <Route path="/admin/" element={<Navigate to="/admin/home" replace />} />
            </Route>

            <Route path="/" element={<Navigate to={role ? `/${role.toLowerCase()}/home` : "/login"} replace />} />

          </Route>

          <Route path="*" element={<FourOhFour />} />
        </Routes>

      </div>
      <Footer />
    </>
  );
}

export default App;
