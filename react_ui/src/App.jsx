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
import SearchEditForm from './BaseComponents/SearchEditForm';
import SelectList from './BaseComponents/SelectList';

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

              <Route path="/admin/register-teacher" element={<SelectList
                title="Register a new teacher."
                requestURL="/auth/admin/register-professor"
                successMsg="Teacher created successfully."
                buttonMsg="Create Teacher"
                formStructure={structures.registerTeacher} />} />

              <Route path="/admin/register-student" element={<SelectList
                title="Register a new student."
                requestURL="/auth/admin/register-student"
                successMsg="Student created successfully."
                buttonMsg="Create Student"
                formStructure={structures.registerStudent} />} />

              <Route path="/admin/set-department-head" element={<SelectList
                title="Set a department head."
                requestURL="/department/admin/{0}/update"
                requestIds={["collegeId"]}
                successMsg="Department head set successfully."
                buttonMsg="Set Department Head"
                formStructure={structures.setDepartmentHead}
                isPut={true} />} />

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

              <Route path="/admin/create-department" element={<SelectList
                title="Create a department."
                requestURL="/department/admin/create"
                successMsg="Department created successfully."
                buttonMsg="Create Department"
                formStructure={structures.createDepartment} />} />

              <Route path="/admin/create-major" element={<SelectList
                title="Create a major."
                requestURL="/major/admin/create"
                successMsg="Major created successfully."
                buttonMsg="Create Major"
                formStructure={structures.createMajor} />} />

              <Route path="/admin/create-course" element={<SelectList
                title="Create a course."
                requestURL="/course/admin/create/"
                successMsg="Course created successfully."
                buttonMsg="Create Course"
                formStructure={structures.createCourse} />} />

              <Route path="/admin/add-teacher-qualification" element={<SelectList
                title="Qualify a teacher to teach a course."
                requestURL="/admin/professorQualification/create"
                successMsg="Teacher qualification added successfully."
                buttonMsg="Add Qualification"
                formStructure={structures.addQualification} />} />

              <Route path="/admin/remove-teacher-qualification" element={<SelectList
                title="Remove teacher qualification."
                requestURL="/admin/professorQualification/delete"
                buttonMsg="Remove Qualification"
                formStructure={structures.removeQualification}
                isDelete={true} />} />

              <Route path="/admin/enroll-student" element={<SelectList
                title="Enroll a student."
                requestURL="/enrollment/admin/create"
                successMsg="Student enrolled successfully."
                buttonMsg="Enroll student"
                formStructure={structures.enrollStudent} />} />

              <Route path="/admin/update-college" element={<SelectList
                title="Update college details."
                requestURL="/college/admin/update/{0}"
                requestIds={["collegeId"]}
                fetchUrl="/college/getById/{0}"
                successMsg="College details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateCollege}
                deleteUrl="/college/admin/{0}/delete"
                deleteWarningMsg="delete this college"
                isPut={true} />} />

              <Route path="/admin/update-faculty" element={<SelectList
                title="Update faculty details."
                requestURL="/faculty/admin/{0}/update"
                requestIds={["facultyId"]}
                fetchUrl="/faculty/getById/{0}"
                successMsg="Faculty details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateFaculty}
                deleteUrl="/faculty/admin/{0}/delete"
                deleteWarningMsg="delete this faculty"
                isPut={true} />} />

              <Route path="/admin/update-department" element={<SelectList
                title="Update department details."
                requestURL="/department/admin/{0}/update"
                requestIds={["departmentId"]}
                fetchUrl="/department/getById/{0}"
                successMsg="Department details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateDepartment}
                deleteUrl="/department/admin/{0}/delete"
                deleteWarningMsg="delete this department"
                isPut={true} />} />

              <Route path="/admin/update-major" element={<SelectList
                title="Update a major's details."
                requestURL="/major/admin/{0}/update"
                requestIds={["majorId"]}
                fetchUrl="/major/getById/{0}"
                successMsg="The major's details are changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateMajor}
                deleteUrl="/major/admin/{0}/delete"
                deleteWarningMsg="delete this major"
                isPut={true} />} />

              <Route path="/admin/update-course" element={<SelectList
                title="Update course details."
                requestURL="/course/admin/{0}/update"
                requestIds={["courseId"]}
                fetchUrl="/course/getById/{0}"
                successMsg="Course details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateCourse}
                deleteUrl="/course/admin/{0}/delete"
                deleteWarningMsg="delete this course"
                isPut={true} />} />

              <Route path="/admin/change-student-major" element={<SelectList
                title="Change a student's major."
                requestURL="/user/update-to-student/{1}/{0}"
                requestIds={["email", "majorId"]}
                successMsg="Student major changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeStudentMajor}
                isPut={true} />} />

              <Route path="/admin/change-teacher-department" element={<SelectList
                title="Change a teacher's department."
                requestURL="/user/update-professor-department/{1}/{0}"
                requestIds={["email", "departmentId"]}
                successMsg="Teacher department changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeTeacherDepartment}
                isPut={true} />} />

              <Route path="/admin/edit-all-user-details" element={<SearchEditForm
                title="Edit all user details."
                requestURL="/user/update/{0}"
                fetchUrl="/user/admin/getUserDetails/{0}"
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeAllUserDetails}
                searchLabel="Enter user's email"
                deleteUrl="user/admin/delete/{0}"
                deleteWarningMsg="delete this user"
              />} />

              <Route path={`/admin/change-user-details`} element={<Form
                title="Change my user details."
                requestURL="/user/update/{0}"
                requestIds={[email]}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.registerUser}
                fetchUrl="/user/getUserDetails"
                isPut={true} />} />

              <Route path="/admin/" element={<Navigate to="/admin/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["STUDENT"]} />}>
              <Route path="/student/dashboard" element={<HomePage title="Student" dashStructure={structures.studentDash} />} />

              <Route path={"/student/change-user-details"} element={<Form
                title="Change my user details."
                requestURL="/user/update/{0}"
                requestIds={[email]}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/user/getUserDetails"
                isPut={true} />} />
              <Route path="/student/" element={<Navigate to="/student/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["PROFESSOR"]} />}>
              <Route path="/professor/dashboard" element={<HomePage title="Teacher" dashStructure={structures.profesorDash} />} />

              <Route path={"/professor/change-user-details"} element={<Form
                title="Change my user details."
                requestURL="/user/update/{0}"
                requestIds={[email]}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/user/getUserDetails"
                isPut={true} />} />
              <Route path="/professor/" element={<Navigate to="/professor/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["USER"]} />}>
              <Route path="/user/dashboard" element={<HomePage title="User" dashStructure={structures.userDash} />} />

              <Route path={"/user/change-user-details"} element={<Form
                title="Change my user details."
                requestURL="/user/update/{0}"
                requestIds={[email]}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/user/getUserDetails"
                isPut={true} />} />
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
