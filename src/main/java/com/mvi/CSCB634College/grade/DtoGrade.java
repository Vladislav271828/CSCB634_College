package com.mvi.CSCB634College.grade;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoGrade {
    private Long id;
    private Long enrollmentId;
    @NotEmpty(message = "Grade name is required.")
    private String name;
    private Double grade;

}
