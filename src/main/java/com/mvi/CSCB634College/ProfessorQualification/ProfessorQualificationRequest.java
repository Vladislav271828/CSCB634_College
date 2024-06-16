package com.mvi.CSCB634College.ProfessorQualification;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorQualificationRequest {

    @NotNull(message = "Professor Id is required.")
    @Positive
    private Integer professorId;

    @NotNull(message = "Course Id is required.")
    @Positive
    private Integer courseId;
}
