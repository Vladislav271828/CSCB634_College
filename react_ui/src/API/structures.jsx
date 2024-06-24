const structures = {
    registerUser: [
        {
            id: "firstname",
            label: "First Name",
            type: "text"
        },
        {
            id: "lastname",
            label: "Last Name",
            type: "text"
        },
        {
            id: "email",
            label: "Email",
            type: "email"
        },
        {
            id: "password",
            label: "Password",
            type: "password"
        },
        {
            id: "role",
            label: "Type of user",
            type: "select",
            options: {
                "USER": "User",
                "ADMIN": "Admin"
            }
        },
    ],
    changeUserDetails: [
        {
            id: "firstname",
            label: "First Name",
            type: "text"
        },
        {
            id: "lastname",
            label: "Last Name",
            type: "text"
        },
        {
            id: "email",
            label: "Email",
            type: "email"
        },
        {
            id: "password",
            label: "Password",
            type: "password"
        },
    ],
    changeAllUserDetails: [
        {
            id: "firstname",
            label: "First Name",
            type: "text"
        },
        {
            id: "lastname",
            label: "Last Name",
            type: "text"
        },
        {
            id: "email",
            label: "Email",
            type: "email"
        },
        {
            id: "password",
            label: "Password",
            type: "password"
        },
        {
            id: "role",
            label: "Type of user",
            type: "select",
            options: {
                "USER": "User",
                "ADMIN": "Admin",
                "STUDENT": "Student",
                "PROFESSOR": "Professor",
            }
        },
    ],
    createCollege: [
        {
            id: "name",
            label: "College Name",
            type: "text"
        },
        {
            id: "address",
            label: "College Address",
            type: "text"
        },
        {
            id: "rectorEmail",
            label: "Email of Rector",
            type: "email",
        },
    ],
    createFaculty: [
        {
            id: "name",
            label: "Faculty Name",
            type: "text"
        },
        {
            id: "collegeId",
            label: "College",
            type: "select",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name"
        },
        // {
        //     id: "test",
        //     label: "Test",
        //     type: "select",
        //     require: "collegeId",
        //     fetchUrl: "/college/admin/burh/{0}",
        //     fetchLabel: "name"
        // }
    ],
    createDepartment: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Major Name",
                    type: "text"
                },
                {
                    id: "facultyId",
                    label: "Faculty",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/faculty/getAllByCollege/{0}",
                    require: ["collegeId"],
                    disabled: true
                }
            ]
        }
    ],
    registerTeacher: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "firstName",
                    label: "First Name",
                    type: "text"
                },
                {
                    id: "lastName",
                    label: "Last Name",
                    type: "text"
                },
                {
                    id: "email",
                    label: "Email",
                    type: "email"
                },
                {
                    id: "password",
                    label: "Password",
                    type: "password"
                },
                {
                    id: "departmentId",
                    label: "Department",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/department/getAllByFaculty/{0}",
                    require: ["facultyId"],
                    disabled: true
                }
            ]
        }
    ],
    setDepartmentHead: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "departmentHeadId",
            selectMsg: "Please select who to be a department head.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "departmentHeadId",
                    label: "Department Head",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                },
                {
                    id: "departmentId",
                    label: "Department",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/department/getAllByFaculty/{0}",
                    require: ["facultyId"],
                    disabled: true
                }
            ]
        }
    ],
    createMajor: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Major Name",
                    type: "text"
                },
                {
                    id: "departmentId",
                    label: "Department",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/department/getAllByFaculty/{0}",
                    require: ["facultyId"],
                    disabled: true
                }
            ]
        }
    ],
    registerStudent: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "firstName",
                    label: "First Name",
                    type: "text"
                },
                {
                    id: "lastName",
                    label: "Last Name",
                    type: "text"
                },
                {
                    id: "email",
                    label: "Email",
                    type: "email"
                },
                {
                    id: "password",
                    label: "Password",
                    type: "password"
                },
                {
                    id: "majorId",
                    label: "Major",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/major/getAllByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                }
            ]
        }
    ],
    createCourse: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Course Name",
                    type: "text"
                },
                {
                    id: "description",
                    label: "Description",
                    type: "text"
                },
                {
                    id: "signature",
                    label: "Signature",
                    type: "text"
                },
                {
                    id: "credits",
                    label: "Credits",
                    type: "select",
                    options: {
                        0: "0 Credits",
                        3: "3 Credits",
                        6: "6 Credits",
                        12: "12 Credits",
                    }
                },
                {
                    id: "majorId",
                    label: "Major",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/major/getAllByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                }
            ]
        }
    ],
    addQualification: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "professorId",
            selectMsg: "Please select which teacher to add a qualification to.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "majorId",
            selectMsg: "Please select the major connected to the course you want to qualify the teacher.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "professorId",
                    label: "Teacher",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByMajor/{0}",
                    require: ["majorId"],
                    disabled: true
                },
            ]
        }
    ],
    removeQualification: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "professorId",
            selectMsg: "Please select which teacher to remove a qualification from.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            fetchUrl: "/admin/professorQualification/getByProfessorId/{0}",
            require: ["professorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "professorId",
                    label: "Teacher",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/admin/professorQualification/getByProfessorId/{0}",
                    require: ["professorId"],
                    disabled: true
                },
            ]
        }
    ],
    enrollStudent: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "studentId",
            selectMsg: "Please select which student to enroll.",
            fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
            require: ["majorId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "professorId",
            selectMsg: "Please select which teacher the student will be enrolled to.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "courseId",
            selectMsg: "Please select which course the student will be enrolled to.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "year",
                    label: "Semester",
                    type: "year"
                },
                {
                    id: "room",
                    label: "Lecture Hall",
                    type: "text"
                },
                {
                    id: "studentId",
                    label: "Student",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
                    require: ["majorId"],
                    disabled: true
                },
                {
                    id: "professorId",
                    label: "Teacher",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByMajor/{0}",
                    require: ["majorId"],
                    disabled: true
                },
            ]
        }
    ],
    updateCollege: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "College Name",
                    type: "text"
                },
                {
                    id: "address",
                    label: "College Address",
                    type: "text"
                },
                {
                    id: "rectorEmail",
                    label: "Email of Rector",
                    type: "email",
                },
            ]
        }
    ],
    updateFaculty: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Faculty Name",
                    type: "text"
                },
                {
                    id: "collegeId",
                    label: "College",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/college/admin/getAll",
                }
            ]
        }
    ],
    updateDepartment: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "facultyId",
            selectMsg: "Please select a new faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
            skip: "Don't change faculty."
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Major Name",
                    type: "text"
                },
                {
                    id: "facultyId",
                    label: "Faculty",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/faculty/getAllByCollege/{0}",
                    require: ["collegeId"],
                    disabled: true
                }
            ]
        }
    ],
    updateMajor: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a new department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
            skip: "Don't change department."

        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Major Name",
                    type: "text"
                },
                {
                    id: "departmentId",
                    label: "Department",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/department/getAllByFaculty/{0}",
                    require: ["facultyId"],
                    disabled: true
                }
            ]
        }
    ],
    updateCourse: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "courseId",
            selectMsg: "Please select a course.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a new major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
            skip: "Don't change major."
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Course Name",
                    type: "text"
                },
                {
                    id: "description",
                    label: "Description",
                    type: "text"
                },
                {
                    id: "signature",
                    label: "Signature",
                    type: "text"
                },
                {
                    id: "credits",
                    label: "Credits",
                    type: "select",
                    options: {
                        0: "0 Credits",
                        3: "3 Credits",
                        6: "6 Credits",
                        12: "12 Credits",
                    }
                },
                {
                    id: "majorId",
                    label: "Major",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/major/getAllByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                }
            ]
        }
    ],
    changeStudentMajor: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "email",
            selectMsg: "Please select a student.",
            fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
            require: ["majorId"],
            altId: "email",
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "majorId",
            selectMsg: "Please select a new major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
            skip: "Don't change major."
        },
        {
            type: "FORM",
            data: [
                {
                    id: "majorId",
                    label: "Major",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/major/getAllByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                }
            ]
        }
    ],
    changeTeacherDepartment: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "email",
            selectMsg: "Please select a teacher.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
            altId: "email",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a new department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
            skip: "Don't change department."
        },
        {
            type: "FORM",
            data: [
                {
                    id: "departmentId",
                    label: "Department",
                    type: "select",
                    fetchLabel: "name",
                    fetchUrl: "/department/getAllByFaculty/{0}",
                    require: ["facultyId"],
                    disabled: true
                }
            ]
        }
    ],
    updateEnrollment: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "studentId",
            selectMsg: "Please select which student you want to change the enrollment of.",
            fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
            require: ["majorId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            id: "year",
            selectMsg: "Please select the year of the enrollment.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "enrollmentId",
            selectMsg: "Please select which enrollment you want to update.",
            fetchUrl: "/enrollment/getAllByStudentAndYear/{0}/{1}",
            require: ["studentId", "year"],
            followUpUrl: "/course/getAllByStudentAndYear/{0}/{1}",
            replacement: "COURSE",
            fetchLabel: "courseId",
            fetchLabelSecond: "id",
        },
        {
            id: "professorId",
            selectMsg: "Please select which teacher the student will be enrolled to.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
            skip: "Don't change teacher."
        },
        {
            id: "courseId",
            selectMsg: "Please select which course the student will be enrolled to.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
            skip: "Don't change course."
        },
        {
            type: "FORM",
            data: [
                {
                    id: "year",
                    label: "Semester",
                    type: "year"
                },
                {
                    id: "room",
                    label: "Lecture Hall",
                    type: "text"
                },
                {
                    id: "studentId",
                    label: "Student",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
                    require: ["majorId"],
                    disabled: true
                },
                {
                    id: "professorId",
                    label: "Teacher",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByMajor/{0}",
                    require: ["majorId"],
                    disabled: true
                },
            ]
        }
    ],
    addAbsence: [
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            require: ["year"],
            fetchUrl: "/course/getAllByProfessorAndYear/{id}/{0}",
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            id: "enrollmentId",
            selectMsg: "Please select the student.",
            fetchUrl: "/enrollment/getAllByProfessorIdAndYearAndCourse/{id}/{0}/{1}",
            require: ["year", "courseId"],
            followUpUrl: "/student/professor/getAllStudentsByCourseId/{1}",
            replacement: "FLNAME",
            fetchLabel: "studentId",
            fetchLabelSecond: "email",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "date",
                    label: "Date",
                    type: "date"
                },
                {
                    id: "note",
                    label: "Absence Note",
                    type: "text"
                },
                {
                    id: "enrollmentId",
                    hide: true
                },
            ]
        }
    ],
    removeAbsence: [
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            require: ["year"],
            fetchUrl: "/course/getAllByProfessorAndYear/{id}/{0}",
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            id: "enrollmentId",
            selectMsg: "Please select the student.",
            fetchUrl: "/enrollment/getAllByProfessorIdAndYearAndCourse/{id}/{0}/{1}",
            require: ["year", "courseId"],
            followUpUrl: "/student/professor/getAllStudentsByCourseId/{1}",
            replacement: "FLNAME",
            fetchLabel: "studentId",
            fetchLabelSecond: "email",
        },
        {
            id: "absenceId",
            selectMsg: "Please select which absence to remove.",
            require: ["enrollmentId"],
            fetchUrl: "/absence/getAllByEnrollment/{0}",
            fetchLabel: "date",
            fetchLabelSecond: "note",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "date",
                    label: "Date",
                    type: "date",
                    disabled: true
                },
                {
                    id: "note",
                    label: "Absence Note",
                    type: "text",
                    disabled: true
                },
            ]
        }
    ],
    addGrade: [
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            require: ["year"],
            fetchUrl: "/course/getAllByProfessorAndYear/{id}/{0}",
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Grade Field Name",
                    type: "text"
                },
                {
                    id: "year",
                    label: "Semester",
                    type: "year",
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByProfessorAndYear/{id}/{year}",
                    require: ["year"],
                    disabled: true
                },
            ]
        }
    ],
    removeGrade: [
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            require: ["year"],
            fetchUrl: "/course/getAllByProfessorAndYear/{id}/{0}",
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            id: "gradeId",
            selectMsg: "Please select which grade to remove.",
            require: ["courseId", "year"],
            fetchUrl: "/grade/getAllByEnrollment/{1}/{0}",
            fetchLabel: "name",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "name",
                    label: "Grade Field Name",
                    type: "text",
                    disabled: true
                },
                {
                    id: "year",
                    label: "Semester",
                    type: "year",
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByProfessorAndYear/{id}/{year}",
                    require: ["year"],
                    disabled: true
                },
            ]
        }
    ],
    gradeStudent: [
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course.",
            require: ["year"],
            fetchUrl: "/course/getAllByProfessorAndYear/{id}/{0}",
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "GRADE"
        }
    ],
    addProgram: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "courseId",
            selectMsg: "Please select the course you want to add to the program.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            id: "professorIds",
            selectMsg: "Please select the teachers that lead the course.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
            multi: true
        },
        {
            type: "FORM",
            data: [
                {
                    id: "year",
                    label: "Semester",
                    type: "year",
                    isNumber: true
                },
                {
                    id: "educationYear",
                    label: "School Year",
                    type: "number"
                },
                {
                    id: "professorIds",
                    label: "Teachers",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    multi: true,
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByMajor/{0}",
                    require: ["majorId"],
                    disabled: true
                },
            ]
        }
    ],
    updateProgram: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "year",
            selectMsg: "Please select the year.",
            fetchUrl: "year",
            fetchLabel: "year",
        },
        {
            id: "programId",
            selectMsg: "Please select which course in the program you want to update.",
            fetchUrl: "/program/getAllBy............/{0}/{1}",
            require: ["studentId", "year", "majorId"],
            followUpUrl: "/course/getAllByMajor/{2}",
            replacement: "COURSE",
            fetchLabel: "courseId",
            fetchLabelSecond: "year",
        },
        {
            type: "FORM",
            data: [
                {
                    id: "year",
                    label: "Semester",
                    type: "year",
                    isNumber: true
                },
                {
                    id: "educationYear",
                    label: "School Year",
                    type: "number"
                },
                {
                    id: "professorIds",
                    label: "Teachers",
                    type: "select",
                    fetchLabel: "firstname",
                    fetchLabelAdd: "lastname",
                    fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
                    require: ["departmentId"],
                    multi: true,
                    disabled: true
                },
                {
                    id: "courseId",
                    label: "Course",
                    type: "select",
                    fetchLabel: "signature",
                    fetchUrl: "/course/getAllByMajor/{0}",
                    require: ["majorId"],
                    disabled: true
                },
            ]
        }
    ],
    viewStudentStats: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "studentId",
            selectMsg: "Please select a student.",
            fetchUrl: "/student/admin/getAllStudentsByMajorId/{0}",
            require: ["majorId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            type: "STATS",
            statistic: "Student",
            require: "studentId"
        }
    ],
    viewTeacherStats: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "professorId",
            selectMsg: "Please select a teacher.",
            fetchUrl: "/professor/admin/getAllProfessorsByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "firstname",
            fetchLabelAdd: "lastname",
            fetchLabelSecond: "email",
        },
        {
            type: "STATS",
            statistic: "Professor",
            require: "professorId"
        }
    ],
    viewCourseStats: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            id: "courseId",
            selectMsg: "Please select a course.",
            fetchUrl: "/course/getAllByMajor/{0}",
            require: ["majorId"],
            fetchLabel: "signature",
            fetchLabelSecond: "name",
        },
        {
            type: "STATS",
            statistic: "Course",
            require: "courseId"
        }
    ],
    viewMajorStats: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            id: "facultyId",
            selectMsg: "Please select a faculty.",
            fetchUrl: "/faculty/getAllByCollege/{0}",
            require: ["collegeId"],
            fetchLabel: "name",
        },
        {
            id: "departmentId",
            selectMsg: "Please select a department.",
            fetchUrl: "/department/getAllByFaculty/{0}",
            require: ["facultyId"],
            fetchLabel: "name",
        },
        {
            id: "majorId",
            selectMsg: "Please select a major.",
            fetchUrl: "/major/getAllByDepartment/{0}",
            require: ["departmentId"],
            fetchLabel: "name",
        },
        {
            type: "STATS",
            statistic: "Major",
            require: "majorId"
        }
    ],
    viewCollegeStats: [
        {
            id: "collegeId",
            selectMsg: "Please select a college.",
            fetchUrl: "/college/admin/getAll",
            fetchLabel: "name",
            fetchLabelSecond: "address"
        },
        {
            type: "STATS",
            statistic: "College",
            require: "collegeId"
        }
    ],
    viewYearStats: [
        {
            id: "year",
            selectMsg: "Please select the year of the enrollment.",
            fetchUrl: "year",
            fetchLabel: "year",
            isNumber: true
        },
        {
            type: "STATS",
            statistic: "Year",
            require: "year"
        }
    ],
    adminDash: [
        {
            link: "register-user",
            name: "Register a new user."
        },
        {
            link: "register-teacher",
            name: "Register a new teacher."
        },
        {
            link: "register-student",
            name: "Register a new student."
        },
        {
            link: "create-college",
            name: "Create a college."
        },
        {
            link: "create-faculty",
            name: "Create a faculty."
        },
        {
            link: "create-department",
            name: "Create a department."
        },
        {
            link: "create-major",
            name: "Create a major."
        },
        {
            link: "create-course",
            name: "Create a course."
        },
        {
            link: "add-teacher-qualification",
            name: "Qualify a teacher to teach a course."
        },
        {
            link: "remove-teacher-qualification",
            name: "Remove teacher qualification."
        },
        {
            link: "add-course-to-program",
            name: "Add a course to the program."
        },
        {
            link: "enroll-student",
            name: "Enroll a student."
        },
        {
            link: "set-department-head",
            name: "Set a department head."
        },
        {
            link: "change-teacher-department",
            name: "Change a teacher's department."
        },
        {
            link: "change-student-major",
            name: "Change a student's major."
        },
        {
            link: "update-college",
            name: "Update college details."
        },
        {
            link: "update-faculty",
            name: "Update faculty details."
        },
        {
            link: "update-department",
            name: "Update department details."
        },
        {
            link: "update-major",
            name: "Update a major's details."
        },
        {
            link: "update-course",
            name: "Update course details."
        },
        {
            link: "update-course-program",
            name: "Update a course in the program."
        },
        {
            link: "update-enrollment",
            name: "Update an enrollment."
        },
        {
            link: "edit-all-user-details",
            name: "Edit all user details."
        },
        {
            link: "view-statistics",
            name: "View statistics."
        }
    ],
    profesorDash: [
        {
            link: "add-grade-field",
            name: "Add a grade field."
        },
        {
            link: "remove-grade-field",
            name: "Remove a grade field."
        },
        {
            link: "grade-students",
            name: "Grade students."
        },
        {
            link: "add-absence",
            name: "Give an absence."
        },
        {
            link: "remove-absence",
            name: "Remove an absence."
        },
    ],
    studentDash: [
        {
            link: "view-grades",
            name: "View your grades."
        },
        {
            link: "view-absences",
            name: "View your absences."
        }
    ],
    rectorDash: [
        {
            link: "view-statistics",
            name: "View statistics."
        }
    ],
    statisticsDash: [
        {
            link: "student-statistics",
            name: "View by students."
        },
        {
            link: "teacher-statistics",
            name: "View by teachers."
        },
        {
            link: "course-statistics",
            name: "View by courses."
        },
        {
            link: "major-statistics",
            name: "View by majors."
        },
        {
            link: "college-statistics",
            name: "View by college."
        },
        {
            link: "year-statistics",
            name: "View by year."
        },
    ],
}

export default structures