package com.mvi.CSCB634College.college;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoCollegeRequest {
    @NotEmpty(message = "College name is required.")
    private String name;
    @NotEmpty(message = "College address is required.")
    private String address;
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String rectorEmail;
}
