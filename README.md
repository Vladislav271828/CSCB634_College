CSCB634_College
# MVI Collegium

A web information system and web app made with React and Spring that is meant for universities, colleges and other higher education institutions.

![preview1](preview1.jpg) ![preview2](preview2.jpg)

## Developers

- **Simeon** - Back-End Development (User Logic, Security Configuration)
- **Vladislav** - Project Manager, Back-End Development, Database
- **Ivaylo** - Front-End Development, UI Design and Branding

## Folder Organization

- `/src` - Source code for the back-end.
- `/react_ui` - Source code for the front-end.
- `/documentation` - Project documentation.

## Run the project locally

*Prerequisites: You must have jdk-17, npm and MySQL server installed and set up on your system.*

Then make sure your MySQL server username and password is configured correctly in the `application.yml` file located in `/src/main/resources`.


To run the **back-end** server, open a terminal in the root directory and execute the command:
```
.\mvnw spring-boot:run
```

Then, to run the **front-end** server, open another terminal in the `/react_ui` directory and execute the following commands:

```powershell
npm i #install dependencies
npm run dev #runs server in developer mode
```

You can then access the app at http://localhost:5173/.

---

**NOTE:** When you first run the project there will be no admin users. To create the first admin user, you need to send an http request:

```http
POST http://localhost:8080/api/v1/auth/register HTTP/1.1
Content-Type: application/json

{
  "firstname": "ADMIN",
  "lastname": "ADMIN",
  "email": "admin@gmail.com",
  "password": "STRONGPASSWORD!@#"
}
```
This request will only work if there are no users in the database.