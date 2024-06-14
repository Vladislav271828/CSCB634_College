package com.mvi.CSCB634College.security.auth;


import com.mvi.CSCB634College.professor.RegisterRequestProfessorDto;
import com.mvi.CSCB634College.student.RegisterRequestDtoStudent;
import com.mvi.CSCB634College.user.ResponseUser;
import com.mvi.CSCB634College.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) throws BadRequestException{
        return ResponseEntity.ok(authenticationService.registerAdmin(request));
    }

    @PostMapping("/admin/register-user")
    public ResponseEntity<AuthenticationResponse> registerUser(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.registerUser(request));
    }

    @PostMapping("/admin/register-professor")
    public ResponseEntity<AuthenticationResponse> registerProfessor(@Valid @RequestBody RegisterRequestProfessorDto request){
        return ResponseEntity.ok(authenticationService.registerProfessor(request));
    }

    @PostMapping("/admin/register-student")
    public ResponseEntity<AuthenticationResponse> registerStudent(@Valid @RequestBody RegisterRequestDtoStudent request){
        return ResponseEntity.ok(authenticationService.registerStudent(request));
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<ResponseUser> getUserDetails() {
        return ResponseEntity.ok(authenticationService.responseUserDetails());
    }


    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    // In AuthenticationController

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestHeader(value = "Authorization") String authorizationHeader) throws BadRequestException {
        return ResponseEntity.ok(authenticationService.refresh(authorizationHeader));
    }


}