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
    adminDash: [
        {
            link: "register-user",
            name: "Register a new user."
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
            name: "Create a department"
        },
        {
            link: "update-college",
            name: "Update college details."
        },
        {
            link: "edit-all-user-details",
            name: "Edit all user details."
        }
    ],
    profesorDash: [
        {
            link: "set-grade-fields",
            name: "Set grade fields."
        },
        {
            link: "grade-students",
            name: "Grade students."
        },
    ]
}

export default structures