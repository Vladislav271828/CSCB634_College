package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.college.DtoCollegeRequest;
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

    @DeleteMapping("/admin/delete/{email}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

}
