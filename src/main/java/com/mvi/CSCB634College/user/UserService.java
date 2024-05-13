package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.security.Role;
import com.mvi.CSCB634College.security.auth.AuthenticationService;
import com.mvi.CSCB634College.security.config.JwtService;
import com.mvi.CSCB634College.security.token.TokenRepository;
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

                    //check if the user is the only admin and is trying to change his role - shouldn't be able to do that because then nobody would be able to change it it has to be done either manually in the db or restart the program.
                    if (userRepository.findAll().toArray().length == 1){
                        throw new BadRequestException("User is the only one with role ADMIN. Change denied.");
                    }

                    user.setRole(dtoUser.getRole());
                    atLeastOneChange = true;

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
         if (user.isEmpty()){
             throw  new UserNotFound("No user with email \"" + email + "\" found.");
         }


        //check if there are other users with admin roles
        boolean areThereAdmins = userRepository.existsByRoleEquals(Role.ADMIN, email);

        //if there are none don;t delete and throw message
        if (!areThereAdmins){
             throw new BadRequestException("Action can't be performed because the user is the only ADMIN in the system.");
         }else{

            //else delete
            user.ifPresent(userRepository::delete);
        }
    }
}
