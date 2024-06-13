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

  const { role, email } = useContext(UserContext);

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
                buttonMsg="Register User"
                formStructure={structures.registerUser} />} />

              <Route path="/admin/create-college" element={<Form
                title="Create a college."
                requestURL="/college/admin/create"
                successMsg="College created successfully."
                buttonMsg="Create College"
                formStructure={structures.createCollege} />} />

              <Route path="/admin/create-faculty" element={<Form
                title="Create a faculty."
                requestURL="/faculty/admin/create"
                successMsg="Faculty created successfully."
                buttonMsg="Create Faculty"
                formStructure={structures.createFaculty} />} />

              <Route path={`/admin/change-user-details`} element={<Form
                title="Change my user details."
                requestURL={"/user/update/" + email}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.registerUser}
                fetchUrl="/auth/getUserDetails" />} />

              <Route path="/admin/" element={<Navigate to="/admin/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["STUDENT"]} />}>
              <Route path="/student/dashboard" element={<HomePage title="Student" dashStructure={[]} />} />

              <Route path={"/student/change-user-details"} element={<Form
                title="Change my user details."
                requestURL={"/user/update/" + email}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/auth/getUserDetails" />} />
              <Route path="/student/" element={<Navigate to="/student/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["PROFESSOR"]} />}>
              <Route path="/professor/dashboard" element={<HomePage title="Professor" dashStructure={[]} />} />

              <Route path={"/professor/change-user-details"} element={<Form
                title="Change my user details."
                requestURL={"/user/update/" + email}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/auth/getUserDetails" />} />
              <Route path="/professor/" element={<Navigate to="/professor/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["USER"]} />}>
              <Route path="/user/dashboard" element={<HomePage title="User" dashStructure={[]} />} />

              <Route path={"/user/change-user-details"} element={<Form
                title="Change my user details."
                requestURL={"/user/update/" + email}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/auth/getUserDetails" />} />
              <Route path="/user/" element={<Navigate to="/user/dashboard" replace />} />
            </Route>

            <Route path="/" element={<Navigate to={role ? `/${role.toLowerCase()}/dashboard` : "/login"} replace />} />

          </Route>

          <Route path="*" element={<FourOhFour />} />
        </Routes>

      </div >
      <Footer />
    </>
  );
}

export default App;
