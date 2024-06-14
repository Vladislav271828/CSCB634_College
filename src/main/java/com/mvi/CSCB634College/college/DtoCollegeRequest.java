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
    private Long id;
    @NotEmpty(message = "College name is required.")
    private String name;
    @NotEmpty(message = "College address is required.")
    private String address;
    @Email(message = "Invalid email format.")
    private String rectorEmail;
}
