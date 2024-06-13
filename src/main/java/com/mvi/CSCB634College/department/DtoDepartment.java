package com.mvi.CSCB634College.department;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoDepartment {
    @NotEmpty(message = "Department name is required.")
    private String name;
    @NotNull(message = "College ID cannot be null")
    @Positive(message = "College ID must be a positive number")
    private Long collegeId;

    private Integer departmentHeadId;

}
