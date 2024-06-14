package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.professor.ProfessorDto;
import com.mvi.CSCB634College.student.StudentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PutMapping("/update/{email}")
    public ResponseEntity<User> updateUser(
            @PathVariable("email") String email,
            @RequestBody @Valid UserDto dtoUser) {
        return ResponseEntity.ok(userService.updateUser(email, dtoUser));
    }

    @PutMapping("/update-professor-department/{email}/{departmentId}")
    public ResponseEntity<ProfessorDto> updateUserToProfessor(
            @PathVariable("email") String email, @PathVariable Long departmentId) {
        return ResponseEntity.ok(userService.updateProfessorDepartment(email, departmentId));
    }

    @PutMapping("/update-to-student/{email}/{majorId}")
    public ResponseEntity<StudentDto> updateToStudent(
            @PathVariable("email") String email, @PathVariable Long majorId) {
        return ResponseEntity.ok(userService.updateStudentMajor(email, majorId));
    }

    @DeleteMapping("/admin/delete/{email}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

}
