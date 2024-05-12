package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String firstname;
    private String lastname;

    @Email(message = "Invalid email format.")
    private String email;

    @Length(min = 8, message = "Password must be at least 8 characters long.")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain an uppercase letter.")
    @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*", message = "Password must contain a special character.")
    private String password;

    private Role role;
}
