package com.mvi.CSCB634College.security.auth;


import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.department.DepartmentRepository;
import com.mvi.CSCB634College.department.DtoDepartment;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.DepartmentNotFoundException;
import com.mvi.CSCB634College.exception.MajorNotFoundException;
import com.mvi.CSCB634College.exception.UserAlreadyExist;
import com.mvi.CSCB634College.major.DtoMajor;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorDto;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.professor.RegisterRequestProfessorDto;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.config.JwtService;
import com.mvi.CSCB634College.security.token.Token;
import com.mvi.CSCB634College.security.token.TokenRepository;
import com.mvi.CSCB634College.security.token.TokenType;
import com.mvi.CSCB634College.student.RegisterRequestDtoStudent;
import com.mvi.CSCB634College.student.Student;
import com.mvi.CSCB634College.student.StudentDto;
import com.mvi.CSCB634College.student.StudentRepository;
import com.mvi.CSCB634College.user.ResponseUser;
import com.mvi.CSCB634College.user.User;
import com.mvi.CSCB634College.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Provides authentication services for user registration, authentication, and token refreshment.
 * This service handles user credentials, token generation and validation, and user authentication state.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;
    private final DepartmentRepository departmentRepository;
    private final MajorRepository majorRepository;


    /**
     * Registers a new user with the provided details from the registration request.
     * Encrypts the user's password, assigns a default role, and generates initial JWT access and refresh tokens.
     *
     * @param request The user registration request containing necessary user details.
     * @return An authentication response containing the JWT access token.
     * @throws RuntimeException if a user with the given email already exists.
     */
    public AuthenticationResponse registerAdmin(RegisterRequest request) throws BadRequestException {

        List<User> usersPage = userRepository.findAll();

        if (!usersPage.isEmpty()) {
            throw new BadRequestException("Please contact an admin.");
        }

        String jwtToken = buildAndSaveUserWithJWT(request, Role.ADMIN);


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    public AuthenticationResponse registerUser(RegisterRequest request) throws BadRequestException {
        if (request.getRole() == null || request.getRole().describeConstable().isEmpty()) {
            throw new BadRequestException("Missing Role required.");
        }

        if (request.getRole().equals(Role.STUDENT) || request.getRole().equals(Role.PROFESSOR)){
            String customErrorMessage = null;
            if (request.getRole().equals(Role.STUDENT)){
                customErrorMessage = "endpoint for the Student creation";
            }
            if (request.getRole().equals(Role.PROFESSOR)){
                customErrorMessage = "endpoint for the Professor creation";
            }
            throw new BadRequestException("Admin can't create user with role " + request.getRole() + " from this endpoint. Use the " + customErrorMessage + " instead.");
        }

        String jwtToken = buildAndSaveUserWithJWT(request, request.getRole());


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse registerProfessor(RegisterRequestProfessorDto request) throws BadRequestException {

        String jwtToken = buildAndSaveUserWithJWTProfessor(request);


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }

    public AuthenticationResponse registerStudent(RegisterRequestDtoStudent request) throws BadRequestException {
        String jwtToken = buildAndSaveUserWithJWTStudent(request);


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    /**
     * Authenticates a user with the provided email and password from the authentication request.
     * Generates new JWT access and refresh tokens if authentication is successful.
     *
     * @param request The authentication request containing the user's credentials.
     * @return An authentication response containing the JWT access token.
     * @throws UsernameNotFoundException if no user is found with the provided email.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        //Find a user or throw 404
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();


        //Authenticates the user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        //Check if user has active tokens
        List<Token> userTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        String jwtToken = null;

        if (userTokens.isEmpty()) {
            // Revoke all tokens
            revokeAllUserTokens(user);//ensures jwt tokens are set to false
            //Create new Refresh Token
            var refreshToken = jwtService.generateRefreshToken(user);
            //Save RefreshToken
            saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//1 day

            //Create new Access Token
            jwtToken = jwtService.generateToken(user);
            //Save Access Token
            saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours

        } else {
            int counter = 0;
            Token activeRefreshToken = null;

            for (Token token : userTokens) {
                //Revoke all access tokens
                if (token.getTokenType().equals(TokenType.ACCESS_TOKEN)) {
                    token.setExpired(true);
                    token.setRevoked(true);
                }
                //Check if the user has active refresh tokens
                if (token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    counter++;
                }

                if (counter == 1 && token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    activeRefreshToken = token;
                } else if (counter > 1 || counter < 0) {
                    activeRefreshToken = null;
                }
            }

            //If the User has more than one refresh token which is not expired - SECURITY RISK - REVOKE ALL
            if (counter > 1) {
                // Revoke all tokens
                revokeAllUserTokens(user);//ensures jwt tokens are set to false

                //Create new Access Token
                jwtToken = jwtService.generateToken(user);
                //Save Access Token
                saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours

                //Create new Refresh Token
                var refreshToken = jwtService.generateRefreshToken(user);
                //Save RefreshToken
                saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//1 day


            } else if (counter == 1) { //If there is one active refresh token then we check if it's active
                if (activeRefreshToken.getExpirationTime().after(new Date())) {

                    // If the user has only one refresh token, and it's active then create a new access token
                    //Create new Access Token
                    jwtToken = jwtService.generateToken(user);
                    //Save Access Token
                    saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours


                } else {
                    // Revoke all tokens
                    revokeAllUserTokens(user);//ensures jwt tokens are set to false
                    //Create new Refresh Token
                    var refreshToken = jwtService.generateRefreshToken(user);
                    //Save RefreshToken
                    saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//1 day

                    //Create new Access Token
                    jwtToken = jwtService.generateToken(user);
                    //Save Access Token
                    saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours


                }
            }
        }


        // Return Access Token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
    }


    /**
     * Refreshes the user's access token using the provided refresh token in the authorization header.
     * Validates the refresh token and generates a new access token if the refresh token is valid.
     *
     * @param authorizationHeader The authorization header containing the bearer access token.
     * @return An authentication response containing the new JWT access token.
     * @throws BadRequestException if the access token is missing, invalid, or not yet expired.
     */
    public AuthenticationResponse refresh(String authorizationHeader) throws BadRequestException {
        //Obtain the token string from the request's header
        String tokenStr;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            tokenStr = authorizationHeader.substring(7); // Remove "Bearer " prefix
        } else {
            throw new BadRequestException("Missing JWT token.");
        }

        //Get the Token object
        Token tokenObj = tokenRepository.findByToken(tokenStr).orElseThrow();

        //Get the User via the Token
        User user = null;
        if (tokenObj.isExpired() && tokenObj.getTokenType().equals(TokenType.ACCESS_TOKEN)) {
            user = userRepository.findUserByToken(tokenObj.getToken()).orElseThrow();
        } else {
            throw new BadRequestException("Can't refresh access token until it expires.");
        }

        // Get all active tokens regardless of their type
        List<Token> activeRefreshTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        // Check all active refresh tokens - we are looking for just one to authorize the creation of a new access token
        boolean hasActiveRefreshToken = false;
        if (!activeRefreshTokens.isEmpty()) {
            for (Token token : activeRefreshTokens) {
                if (token.getTokenType().equals(TokenType.REFRESH_TOKEN)) {
                    hasActiveRefreshToken = true;
                    break;
                }
            }
        }

        if (hasActiveRefreshToken) {
            //generate a new access token and save it
            var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

            saveUserToken(user, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .build();
        } else {
            throw new InternalError("Invalid Refresh Token. Log in again.");
        }


    }


    /**
     * Revokes all valid tokens associated with the specified user.
     * This method sets both 'expired' and 'revoked' flags to true for all valid tokens of the user,
     * effectively invalidating these tokens. The changes are persisted to the database.
     *
     * @param user The user whose tokens are to be revoked.
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    /**
     * Creates and saves a token for the given user in the database.
     * This method constructs a new token object with the specified attributes and saves it to the database.
     * It is used to generate both access and refresh tokens as part of the authentication and token refresh processes.
     *
     * @param savedUser The user for whom the token is being created.
     * @param jwtToken  The token string.
     * @param tokenType The type of token being created (e.g., ACCESS_TOKEN, REFRESH_TOKEN).
     * @param issuedAt  The date and time when the token was issued.
     * @param expiresAt The date and time when the token will expire.
     */
    private void saveUserToken(User savedUser, String jwtToken, TokenType tokenType, Date issuedAt, Date expiresAt) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(tokenType)
                .revoked(false)
                .expired(false)
                .ExpirationTime(expiresAt)
                .IssuedAt(issuedAt)
                .build();//here we create a Token object

        tokenRepository.save(token);//we save the token object in our database
    }


    /**
     * Retrieves the user associated with the current security context.
     * Useful for operations that require the currently authenticated user.
     *
     * @return The currently authenticated user.
     * @throws UsernameNotFoundException if the user cannot be found in the context.
     */
    public User getCurrentlyLoggedUser() { //throws the cringe
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName(); // This returns the username/email of the authenticated user

        return userRepository.findByEmail(email)
                .orElseThrow();
    }


    /**
     * Retrieves a user based on a provided JWT token string.
     * This method is typically used to associate a JWT token with a user entity.
     *
     * @param jwtToken The JWT token associated with the user.
     * @return The user associated with the provided JWT token.
     * @throws UsernameNotFoundException if no user is found associated with the token.
     */
    public User getUserByJwtToken(String jwtToken) {
        System.out.println(userRepository.findByTokens_Token(jwtToken).orElseThrow());
        return userRepository.findByTokens_Token(jwtToken).orElseThrow();
    }

    /**
     * Provides the details of the currently authenticated user.
     * A convenience method that wraps the logic of {@link #getCurrentlyLoggedUser()}.
     *
     * @return The details of the currently authenticated user.
     */
    public ResponseUser responseUserDetails() {

        User user = getCurrentlyLoggedUser();

        ResponseUser responseUser = new ResponseUser();
        responseUser.setId(user.getId());
        responseUser.setEmail(user.getEmail());
        responseUser.setFirstname(user.getFirstname());
        responseUser.setLastname(user.getLastname());
        responseUser.setRole(user.getRole());

        if (user.getRole().equals(Role.PROFESSOR)) {
            Professor professor = professorRepository.findProfessorByUserId(user.getId()).orElseThrow();
            Department department = departmentRepository.findDepartmentByProfessorId(professor.getId(), professor.getId()).orElseThrow();

            DtoDepartment departmentResponse = new DtoDepartment();

            Integer departmentHeadId = null;
            if (department.getDepartmentHead() != null) {
                departmentHeadId = department.getDepartmentHead().getId();
            }
            departmentResponse.setDepartmentHeadId(departmentHeadId);
            departmentResponse.setFacultyId(department.getFaculty().getId());
            departmentResponse.setName(department.getName());

            ProfessorDto professorResponse = new ProfessorDto();
            professorResponse.setId(professor.getId());
            professorResponse.setDepartment(departmentResponse);
        } else {
            responseUser.setProfessor(new ProfessorDto());
        }

        if (user.getRole().equals(Role.STUDENT)) {
            Student student = studentRepository.findStudentByUserId(user.getId()).orElseThrow();
            Major major = majorRepository.findMajorById(student.getMajor().getId()).orElseThrow();

            DtoMajor majorResponse = new DtoMajor();
            major.setId(major.getId());
            majorResponse.setName(major.getName());
            majorResponse.setDepartmentId(major.getDepartment().getId());

            StudentDto studentResponse = new StudentDto();
            studentResponse.setId(student.getId());
            studentResponse.setMajor(majorResponse);
        } else {
            responseUser.setStudent(null);
        }

        return responseUser;
    }

    private String buildAndSaveUserWithJWT(RegisterRequest request, Role role){
        //create user with request data
        //Check if new user is unique
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExist("User already exists.");
        }
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .tokens(new ArrayList<>())
                .build();

        var savedUser = userRepository.save(user); //save user in the db

        var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

        saveUserToken(savedUser, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours


        // In AuthenticationService's register and authenticate methods

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//save the token for 1 day

        return jwtToken;
    }


    private String buildAndSaveUserWithJWTProfessor(RegisterRequestProfessorDto request) {
        //create user with request data
        //Check if new user is unique

        if (!departmentRepository.doesDepartmentExist(request.getDepartmentId())) {
            throw new DepartmentNotFoundException("No department was found with id " + request.getDepartmentId() + ".");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExist("User already exists.");
        }
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PROFESSOR)
                .tokens(new ArrayList<>())
                .build();

        var savedUser = userRepository.save(user); //save user in the db

        var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

        saveUserToken(savedUser, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours


        // In AuthenticationService's register and authenticate methods

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//save the token for 1 day


        //create professor in db
        Professor professor = new Professor();
        professor.setUser(user);
        professor.setDepartment(departmentRepository.findById(request.getDepartmentId()).orElseThrow());
        professorRepository.save(professor);


        return jwtToken;
    }


    private String buildAndSaveUserWithJWTStudent(RegisterRequestDtoStudent request) {
        //create user with request data
        //Check if new user is unique

        if (!majorRepository.existsById(request.getMajorId())) {
            throw new MajorNotFoundException("No department was found with id " + request.getMajorId() + ".");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExist("User already exists.");
        }
        var user = User.builder()
                .firstname(request.getFirstName())
                .lastname(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.STUDENT)
                .tokens(new ArrayList<>())
                .build();

        var savedUser = userRepository.save(user); //save user in the db

        var jwtToken = jwtService.generateToken(user); //generate a JWT Token for the user's session (If the user logs out the token will be marked as invalid, if the user authenticates again a new token will be created and the old one will be updated to be invalid)

        saveUserToken(savedUser, jwtToken, TokenType.ACCESS_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 30));//half an hours


        // In AuthenticationService's register and authenticate methods

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(user, refreshToken, TokenType.REFRESH_TOKEN, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));//save the token for 1 day


        //create student in db
        Student student = new Student();
        student.setUser(user);
        student.setMajor(majorRepository.findMajorById(request.getMajorId()).orElseThrow());
        studentRepository.save(student);


        return jwtToken;
    }


}