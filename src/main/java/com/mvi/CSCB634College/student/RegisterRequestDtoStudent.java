package com.mvi.CSCB634College.student;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDtoStudent {
    @NotEmpty(message = "First name is required.")
    @Length(min = 3, message = "First name must be longer than 3 characters.")
    @Length(max = 18, message = "First name is too long, must be less than 18 characters.")
    private String firstName;
    private String lastName;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotEmpty(message = "Password is required.")
    @Length(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain an uppercase letter.")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "Password must contain a special character.")
    private String password;

    @NotNull(message = "Major Id is required.")
    @Positive
    private Long majorId;

    @Positive
    private Integer educationYear;

}
