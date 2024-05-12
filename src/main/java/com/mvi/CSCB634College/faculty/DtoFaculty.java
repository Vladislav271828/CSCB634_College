package com.mvi.CSCB634College.faculty;

import com.mvi.CSCB634College.college.College;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoFaculty {

    private Long id;
    @NotEmpty(message = "Faculty name is required.")
    private String name;
    private Long collegeId;
}
