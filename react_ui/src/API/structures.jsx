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
                "STUDENT": "Student",
                "PROFESSOR": "Professor",
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
            type: "email"
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
        //     fetchUrl: "/college/admin/burh/{id}",
        //     fetchLabel: "name"
        // }
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
    ],
}

export default structures