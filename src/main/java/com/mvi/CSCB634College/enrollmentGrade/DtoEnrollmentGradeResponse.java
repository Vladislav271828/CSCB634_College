package com.mvi.CSCB634College.enrollmentGrade;

import com.mvi.CSCB634College.grade.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoEnrollmentGradeResponse {

    private Long enrollmentId;
    private Grade grade;
    private Integer score;
}
