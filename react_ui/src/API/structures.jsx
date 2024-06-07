const structures = {
    registerUser: [
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
    adminDash: [
        {
            link: "register-user",
            name: "Register a new user."
        },
    ],
}

export default structures