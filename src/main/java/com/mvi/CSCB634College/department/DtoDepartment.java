package com.mvi.CSCB634College.department;

import jakarta.validation.constraints.NotEmpty;
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
    private Long collegeId;
    private Integer departmentHeadId;

}
