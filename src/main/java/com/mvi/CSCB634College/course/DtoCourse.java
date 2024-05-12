package com.mvi.CSCB634College.course;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoCourse {

    private Integer id;

    @NotEmpty(message = "Course name is required.")
    private String name;
    private String description;
    private String signature;
    private Integer credits;

    private Integer majorId;

    
}
