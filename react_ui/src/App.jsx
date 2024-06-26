import { Routes, Route, Navigate } from 'react-router-dom';
import PrivateRoute from './Login/PrivateRoute';
import PersistLogin from './Login/PersistLogin'

import Dashboard from './BaseComponents/Dashboard';
import Login from './Login/Login';
import Header from './BaseComponents/Header';
import Footer from './BaseComponents/Footer';
import FourOhFour from './BaseComponents/404';
import { useContext } from 'react';
import UserContext from './Context/UserProvider';
import Form from './BaseComponents/Form';

import structures from "./API/structures"
import SearchEditForm from './SpecialComponents/SearchEditForm';
import SelectList from './BaseComponents/SelectList';
import AbsGradeTable from './SpecialComponents/AbsGradeTable';

function App() {

  const { role, email } = useContext(UserContext);

  return (
    <>
      <Header />
      <div className='app-content'>

        <Routes>
          <Route path="/login" element={<Login />} />

          <Route element={<PersistLogin />}>

            <Route element={<PrivateRoute allowedRoles={["ADMIN"]} />}>
              <Route path="/admin/dashboard" element={<Dashboard title="Admin Dashboard" dashStructure={structures.adminDash} />} />

              <Route path="/admin/user/register-user" element={<Form
                title="Register a new user."
                requestURL="/auth/admin/register-user"
                successMsg="User registered successfully."
                buttonMsg="Register User"
                formStructure={structures.registerUser} />} />

              <Route path="/admin/user/register-teacher" element={<SelectList
                title="Register a new teacher."
                requestURL="/auth/admin/register-professor"
                successMsg="Teacher created successfully."
                buttonMsg="Create Teacher"
                formStructure={structures.registerTeacher} />} />

              <Route path="/admin/user/register-student" element={<SelectList
                title="Register a new student."
                requestURL="/auth/admin/register-student"
                successMsg="Student created successfully."
                buttonMsg="Create Student"
                formStructure={structures.registerStudent} />} />

              <Route path="/admin/user/set-department-head" element={<SelectList
                title="Set a department head."
                requestURL="/department/admin/{0}/update"
                requestIds={["collegeId"]}
                successMsg="Department head set successfully."
                buttonMsg="Set Department Head"
                formStructure={structures.setDepartmentHead}
                isPut={true} />} />

              <Route path="/admin/create/college" element={<Form
                title="Create a college."
                requestURL="/college/admin/create"
                successMsg="College created successfully."
                buttonMsg="Create College"
                formStructure={structures.createCollege} />} />

              <Route path="/admin/create/faculty" element={<Form
                title="Create a faculty."
                requestURL="/faculty/admin/create"
                successMsg="Faculty created successfully."
                buttonMsg="Create Faculty"
                formStructure={structures.createFaculty} />} />

              <Route path="/admin/create/department" element={<SelectList
                title="Create a department."
                requestURL="/department/admin/create"
                successMsg="Department created successfully."
                buttonMsg="Create Department"
                formStructure={structures.createDepartment} />} />

              <Route path="/admin/create/major" element={<SelectList
                title="Create a major."
                requestURL="/major/admin/create"
                successMsg="Major created successfully."
                buttonMsg="Create Major"
                formStructure={structures.createMajor} />} />

              <Route path="/admin/create/course" element={<SelectList
                title="Create a course."
                requestURL="/course/admin/create/"
                successMsg="Course created successfully."
                buttonMsg="Create Course"
                formStructure={structures.createCourse} />} />

              <Route path="/admin/user/add-teacher-qualification" element={<SelectList
                title="Qualify a teacher to teach a course."
                requestURL="/admin/professorQualification/create"
                successMsg="Teacher qualification added successfully."
                buttonMsg="Add Qualification"
                formStructure={structures.addQualification} />} />

              <Route path="/admin/user/remove-teacher-qualification" element={<SelectList
                title="Remove teacher qualification."
                requestURL="/admin/professorQualification/delete"
                buttonMsg="Remove Qualification"
                formStructure={structures.removeQualification}
                isDelete={true} />} />

              <Route path="/admin/user/enroll-student" element={<SelectList
                title="Enroll a student."
                requestURL="/enrollment/admin/create"
                successMsg="Student enrolled successfully."
                buttonMsg="Enroll student"
                formStructure={structures.enrollStudent} />} />

              <Route path="/admin/add-course-to-program" element={<SelectList
                title="Add a course to the program."
                requestURL="/program/admin/create"
                successMsg="Course added successfully to program."
                buttonMsg="Add course"
                formStructure={structures.addProgram} />} />

              <Route path="/admin/update/college" element={<SelectList
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

              <Route path="/admin/update/faculty" element={<SelectList
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

              <Route path="/admin/update/department" element={<SelectList
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

              <Route path="/admin/update/major" element={<SelectList
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

              <Route path="/admin/update/course" element={<SelectList
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

              <Route path="/admin/user/enrollment" element={<SelectList
                title="Update an enrollment."
                requestURL="/enrollment/admin/{0}/update"
                requestIds={["enrollmentId"]}
                fetchUrl="/enrollment/getById/{0}"
                successMsg="Enrollment details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateEnrollment}
                deleteUrl="/enrollment/admin/{0}/delete"
                deleteWarningMsg="delete this enrollment"
                isPut={true} />} />

              <Route path="/admin/user/change-student-major" element={<SelectList
                title="Change a student's major."
                requestURL="/user/update-to-student/{1}/{0}"
                requestIds={["email", "majorId"]}
                successMsg="Student major changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeStudentMajor}
                isPut={true} />} />

              <Route path="/admin/user/change-teacher-department" element={<SelectList
                title="Change a teacher's department."
                requestURL="/user/update-professor-department/{1}/{0}"
                requestIds={["email", "departmentId"]}
                successMsg="Teacher department changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeTeacherDepartment}
                isPut={true} />} />

              <Route path="/admin/user/update-any-user" element={<SearchEditForm
                title="Update any user."
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

              <Route path="/admin/change-course-program" element={<SelectList
                title="Change a course in the program."
                requestURL="/program/admin/{0}/update"
                fetchUrl="/program/getById/{0}"
                requestIds={["programId"]}
                successMsg="Course in program changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.updateProgram}
                deleteUrl="/program/admin/{0}/delete"
                deleteWarningMsg="delete this course from the program"
                isPut={true} />} />

              <Route path="/admin/view-statistics" element={<Dashboard
                title="View Statistics."
                dashStructure={structures.statisticsDash}
                sub={true} />} />

              <Route path="/admin/create" element={<Dashboard
                title="Create."
                dashStructure={structures.adminCreateDash}
                sub={true} />} />

              <Route path="/admin/update" element={<Dashboard
                title="Update."
                dashStructure={structures.adminUpdateDash}
                sub={true} />} />

              <Route path="/admin/user" element={<Dashboard
                title="User Options."
                dashStructure={structures.adminUserDash}
                sub={true} />} />

              <Route path="/admin/view-statistics/student" element={<SelectList
                title="View Statistics By Student."
                formStructure={structures.viewStudentStats} />} />

              <Route path="/admin/view-statistics/teacher" element={<SelectList
                title="View Statistics By Teacher."
                formStructure={structures.viewTeacherStats} />} />

              <Route path="/admin/view-statistics/course" element={<SelectList
                title="View Statistics By Course."
                formStructure={structures.viewCourseStats} />} />

              <Route path="/admin/view-statistics/major" element={<SelectList
                title="View Statistics By Major."
                formStructure={structures.viewMajorStats} />} />

              <Route path="/admin/view-statistics/college" element={<SelectList
                title="View Statistics By College."
                formStructure={structures.viewCollegeStats} />} />

              <Route path="/admin/view-statistics/year" element={<SelectList
                title="View Statistics By Year."
                formStructure={structures.viewYearStats} />} />

              <Route path="/admin/view-program" element={<SelectList
                title="View Program."
                formStructure={structures.viewProgram} />} />

              <Route path="/admin/" element={<Navigate to="/admin/dashboard" replace />} />
            </Route>

            <Route element={<PrivateRoute allowedRoles={["STUDENT"]} />}>
              <Route path="/student/dashboard" element={<Dashboard title="Student Dashboard" dashStructure={structures.studentDash} />} />

              <Route path="/student/view-absences" element={<AbsGradeTable
                title="View your absences."
                type="ABSENCE" />} />

              <Route path="/student/view-grades" element={<AbsGradeTable
                title="View your grades."
                type="GRADE" />} />

              <Route path="/student/view-program" element={<SelectList
                title="View Program."
                formStructure={structures.viewProgram} />} />

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
              <Route path="/professor/dashboard" element={<Dashboard title="Teacher Dashboard" dashStructure={structures.profesorDash} />} />

              <Route path="/professor/view-program" element={<SelectList
                title="View Program."
                formStructure={structures.viewProgram} />} />

              <Route path="/professor/add-absence" element={<SelectList
                title="Give an absence."
                requestURL="/absence/professor/create"
                successMsg="Absence added successfully."
                buttonMsg="Add Absence"
                formStructure={structures.addAbsence} />} />

              <Route path="/professor/add-grade-field" element={<SelectList
                title="Add a grade field."
                requestURL="/grade/professor/create"
                successMsg="Grade field added successfully."
                buttonMsg="Add Grade Field"
                formStructure={structures.addGrade} />} />

              <Route path="/professor/grade-students" element={<SelectList
                title="Grade Students."
                formStructure={structures.gradeStudent} />} />

              <Route path="/professor/remove-grade-field" element={<SelectList
                title="Remove a grade field."
                requestURL="/grade/professor/{0}/delete"
                requestIds={["gradeId"]}
                buttonMsg="Remove Grade Field"
                formStructure={structures.removeGrade}
                fetchUrl="/grade/getById/{0}"
                isDelete={true} />} />

              <Route path="/professor/remove-absence" element={<SelectList
                title="Remove an absence."
                requestURL="/absence/professor/{0}/delete"
                requestIds={["absenceId"]}
                buttonMsg="Remove Absence"
                formStructure={structures.removeAbsence}
                fetchUrl="/absence/getById/{0}"
                isDelete={true} />} />

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
              <Route path="/user/dashboard" element={<Dashboard title="User Dashboard" dashStructure={[]} />} />

              <Route path="/user/view-program" element={<SelectList
                title="View Program."
                formStructure={structures.viewProgram} />} />

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

            <Route element={<PrivateRoute allowedRoles={["RECTOR"]} />}>
              <Route path="/rector/dashboard" element={<Dashboard title="Rector Dashboard" dashStructure={structures.rectorDash} />} />

              <Route path="/rector/view-program" element={<SelectList
                title="View Program."
                formStructure={structures.viewProgram} />} />

              <Route path="/rector/view-statistics" element={<Dashboard
                title="View Statistics."
                dashStructure={structures.statisticsDash}
                sub={true} />} />

              <Route path="/rector/view-statistics/student" element={<SelectList
                title="View Statistics By Student."
                formStructure={structures.viewStudentStats} />} />

              <Route path="/rector/view-statistics/teacher" element={<SelectList
                title="View Statistics By Teacher."
                formStructure={structures.viewTeacherStats} />} />

              <Route path="/rector/view-statistics/course" element={<SelectList
                title="View Statistics By Course."
                formStructure={structures.viewCourseStats} />} />

              <Route path="/rector/view-statistics/major" element={<SelectList
                title="View Statistics By Major."
                formStructure={structures.viewMajorStats} />} />

              <Route path="/rector/view-statistics/college" element={<SelectList
                title="View Statistics By College."
                formStructure={structures.viewCollegeStats} />} />

              <Route path="/rector/view-statistics/year" element={<SelectList
                title="View Statistics By Year."
                formStructure={structures.viewYearStats} />} />

              <Route path={"/rector/change-user-details"} element={<Form
                title="Change my user details."
                requestURL="/user/update/{0}"
                requestIds={[email]}
                successMsg="User details changed successfully."
                buttonMsg="Save Changes"
                formStructure={structures.changeUserDetails}
                fetchUrl="/user/getUserDetails"
                isPut={true} />} />
              <Route path="/rector/" element={<Navigate to="/rector/dashboard" replace />} />
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
