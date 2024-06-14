package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.department.DepartmentRepository;
import com.mvi.CSCB634College.department.DtoDepartment;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.DepartmentNotFoundException;
import com.mvi.CSCB634College.exception.MajorNotFoundException;
import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.major.DtoMajor;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorDto;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.config.JwtService;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.student.Student;
import com.mvi.CSCB634College.student.StudentDto;
import com.mvi.CSCB634College.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;


    public User updateUser(String email, UserDto dtoUser) {
        User currentUser = authenticationService.getCurrentlyLoggedUser();

        //To keep track on if there was a change made - to prevent unnecessary db operations
        boolean atLeastOneChange = false;

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User with email " + dtoUser.getEmail() + " not found"));

        // Check if at least one field is provided
        if (dtoUser.getFirstname() == null && dtoUser.getLastname() == null && dtoUser.getEmail() == null && dtoUser.getPassword() == null && dtoUser.getRole() == null) {
            throw new BadRequestException("At least one field must be updated.");
        }

        // Update name if it's provided and unique
        if (dtoUser.getFirstname() != null && !dtoUser.getFirstname().isBlank()) {
            if (user.getFirstname().equals(dtoUser.getFirstname())) {
                throw new BadRequestException("User already has first name \"" + dtoUser.getFirstname() + "\".");
            }
            user.setFirstname(dtoUser.getFirstname());
            atLeastOneChange = true;
        }

        // Update name if it's provided and unique
        if (dtoUser.getLastname() != null && !dtoUser.getLastname().isBlank()) {
            if (user.getLastname().equals(dtoUser.getLastname())) {
                throw new BadRequestException("User already has last name \"" + dtoUser.getLastname() + "\".");
            }
            user.setLastname(dtoUser.getLastname());
            atLeastOneChange = true;
        }

        // Update email if it's provided and unique
        if (dtoUser.getEmail() != null && !dtoUser.getEmail().isBlank()) {
            if (user.getEmail().equals(dtoUser.getEmail())) {
                throw new BadRequestException("User already has email \"" + dtoUser.getEmail() + "\".");
            }

            if (userRepository.existsByEmail(dtoUser.getEmail())) {
                throw new BadRequestException("User with email \"" + dtoUser.getEmail() + "\" already exists.");
            }
            user.setEmail(dtoUser.getEmail());
            atLeastOneChange = true;
        }

        // Update password
        if (dtoUser.getPassword() != null && !dtoUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dtoUser.getPassword()));
            atLeastOneChange = true;
        }


        ////TODO: Check if collage dep and major exist if trying to change to student
        ////TODO: Check if collage dep and major exist if trying to change to professor
        // Update role if it's provided and unique
        if (dtoUser.getRole() != null && !dtoUser.getRole().toString().isBlank()) {

            //check if the user is admin to perform the role change
            if (!currentUser.getRole().equals(Role.ADMIN)) {
                throw new BadRequestException("User doesn't have ADMIN authorization to perform a role change.");
            }

            //check if provided role is part of the enum array (because we are accepting Role as object and not string this will not be of importance because the program will throw exception on mapping failure, but it's good to have)
            if (Role.valueOf(dtoUser.getRole().toString()).describeConstable().isPresent()) {
                String role = Role.valueOf(dtoUser.getRole().toString()).describeConstable().get().constantName();

                if (!role.isEmpty()) {
                    if (user.getRole().equals(dtoUser.getRole())) {
                        throw new BadRequestException("User is already with role \"" + user.getRole() + "\".");
                    }

                    //check if the user is the only admin and is trying to change his role - shouldn't be able to do that because then nobody would be able to change it, it has to be done either manually in the db or restart the program.
                    if (userRepository.findAll().toArray().length == 1) {
                        throw new BadRequestException("User is the only one with role ADMIN. Change denied.");
                    }


                    // Check if we are changing the role to ADMIN or USER from STUDENT or PROFESSOR
                    if ((dtoUser.getRole().equals(Role.ADMIN) || dtoUser.getRole().equals(Role.USER)) &&
                            (user.getRole().equals(Role.STUDENT) || user.getRole().equals(Role.PROFESSOR))) {

                        // Handle transition from STUDENT to ADMIN or USER
                        if (user.getRole().equals(Role.STUDENT)) {
                            Student student = studentRepository.findStudentByUserId(user.getId()).orElseThrow();
                            studentRepository.deleteById(student.getId());
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }

                        // Handle transition from PROFESSOR to ADMIN or USER
                        if (user.getRole().equals(Role.PROFESSOR)) {
                            Professor professor = professorRepository.findProfessorByUserId(user.getId()).orElseThrow();
                            professorRepository.deleteById(professor.getId());
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }
                    }

                    // Check if we are changing the role to PROFESSOR
                    if (dtoUser.getRole().equals(Role.PROFESSOR)) {

                        // Handle transition from ADMIN or USER to PROFESSOR
                        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.USER)) {
                            Professor newProfessor = new Professor();
                            newProfessor.setUser(user);
                            professorRepository.save(newProfessor);
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }

                        // Handle transition from STUDENT to PROFESSOR
                        if (user.getRole().equals(Role.STUDENT)) {
                            Student student = studentRepository.findStudentByUserId(user.getId()).orElseThrow();
                            studentRepository.deleteById(student.getId());
                            Professor newProfessor = new Professor();
                            newProfessor.setUser(user);
                            professorRepository.save(newProfessor);
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }
                    }

                    // Check if we are changing the role to STUDENT
                    if (dtoUser.getRole().equals(Role.STUDENT)) {

                        // Handle transition from ADMIN or USER to STUDENT
                        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.USER)) {
                            Student newStudent = new Student();
                            newStudent.setUser(user);
                            studentRepository.save(newStudent);
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }

                        // Handle transition from PROFESSOR to STUDENT
                        if (user.getRole().equals(Role.PROFESSOR)) {
                            Professor professor = professorRepository.findProfessorByUserId(user.getId()).orElseThrow();
                            professorRepository.deleteById(professor.getId());
                            Student newStudent = new Student();
                            newStudent.setUser(user);
                            studentRepository.save(newStudent);
                            user.setRole(dtoUser.getRole());
                            atLeastOneChange = true;
                        }
                    }

                    // Check if we are changing the role to ADMIN or USER from a non-STUDENT and non-PROFESSOR role
                    if ((dtoUser.getRole().equals(Role.ADMIN) || dtoUser.getRole().equals(Role.USER)) &&
                            (!user.getRole().equals(Role.STUDENT) && !user.getRole().equals(Role.PROFESSOR))) {
                        user.setRole(dtoUser.getRole());
                        atLeastOneChange = true;
                    }


                } else {
                    throw new BadRequestException("No such role \"" + dtoUser.getRole().toString() + "\" found.");
                }
            }
        }


        //check if there was a change before putting load on the db
        if (atLeastOneChange) {

            return userRepository.save(user);
        }

        //otherwise return the user - edge case
        return user;
    }


    public void deleteUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFound("No user with email \"" + email + "\" found.");
        }


        //check if there are other users with admin roles
        boolean areThereAdmins = userRepository.existsByRoleEquals(Role.ADMIN, email);

        //if there are none don;t delete and throw message
        if (!areThereAdmins) {
            throw new BadRequestException("Action can't be performed because the user is the only ADMIN in the system.");
        } else {

            //else delete
            user.ifPresent(userRepository::delete);
        }
    }

    public ProfessorDto updateProfessorDepartment(String email, Long departmentId) {
        User currentUser = authenticationService.getCurrentlyLoggedUser();

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User with email " + email + " not found"));


        //If the user is already a professor
        if (!user.getRole().equals(Role.PROFESSOR)) {
            throw new BadRequestException("User is not a professor");
        }
        //check if the user is admin to perform the role change
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("User doesn't have ADMIN authorization to perform a role change.");
        }

        //check if the user is in a department
        if (professorRepository.findProfessorByUserId(user.getId()).orElseThrow().getDepartment() == null){
            //check if the department is real
            if (!departmentRepository.doesDepartmentExist(departmentId)) {
                throw new DepartmentNotFoundException("Department with id " + departmentId + " doesn't exist.");
            }

        }else{
            //check if the department they are in is the one we are trying to change it to
            if (departmentId.equals(departmentRepository.findById(professorRepository.findProfessorByUserId(user.getId()).orElseThrow().getDepartment().getId()).orElseThrow().getId())){
                throw new BadRequestException("Professor is already in this department.");
            }
        }


        if (!departmentRepository.doesDepartmentExist(departmentId)) {
            throw new DepartmentNotFoundException("Department with id " + departmentId + " doesn't exist.");
        }

        Professor professor = professorRepository.findProfessorByUserId(user.getId()).orElseThrow();
        Department department = departmentRepository.findById(departmentId).orElseThrow();

        professor.setDepartment(department);
        professorRepository.save(professor);

        DtoDepartment departmentDto = new DtoDepartment();
        departmentDto.setName(department.getName());
        departmentDto.setCollegeId(department.getCollege().getId());
        Integer departmentHeadId = null;
        if (department.getDepartmentHead() != null){
            departmentHeadId = department.getDepartmentHead().getId();
        }
        departmentDto.setDepartmentHeadId(departmentHeadId);

        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setDepartment(departmentDto);
        professorDto.setId(professor.getId());
        return professorDto;
    }





    public StudentDto updateStudentMajor(String email, Long majorId) {

        User currentUser = authenticationService.getCurrentlyLoggedUser();

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFound("User with email " + email + " not found"));


        //If the user is already a student
        if (!user.getRole().equals(Role.STUDENT)) {
            throw new BadRequestException("User is not a student.");
        }
        //check if the user is admin to perform the role change
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new BadRequestException("User doesn't have ADMIN authorization to perform a role change.");
        }

        //check if the user is in a Major
        if (studentRepository.findStudentByUserId(user.getId()).orElseThrow().getMajor() == null){
            //check if the major is real
            if (!majorRepository.doesMajorExist(majorId)) {
                throw new MajorNotFoundException("Major with id " + majorId + " doesn't exist.");
            }

        }else{
            //check if the department they are in is the one we are trying to change it to
            if (majorId.equals(majorRepository.findMajorById(studentRepository.findStudentByUserId(user.getId()).orElseThrow().getMajor().getId()).orElseThrow().getId())){
                throw new BadRequestException("Student is already in this Major.");
            }
        }


        if (!majorRepository.doesMajorExist(majorId)) {
            throw new MajorNotFoundException("Major with id " + majorId + " doesn't exist.");
        }

        Student student = studentRepository.findStudentByUserId(user.getId()).orElseThrow();
        Major major = majorRepository.findById(majorId).orElseThrow();

        student.setMajor(major);
        studentRepository.save(student);

        DtoMajor majorDto = new DtoMajor();
        majorDto.setName(major.getName());
        majorDto.setId(major.getId());
        majorDto.setDepartmentId(major.getDepartment().getId());

        StudentDto studentDto = new StudentDto();
        studentDto.setId(user.getId());
        studentDto.setMajor(majorDto);
        return studentDto;
    }
}
