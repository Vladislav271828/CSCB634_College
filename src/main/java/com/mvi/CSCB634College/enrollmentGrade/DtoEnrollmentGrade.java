package com.mvi.CSCB634College.enrollmentGrade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoEnrollmentGrade {

    private Long enrollmentId;
    private Long gradeId;
    private Integer score;
}
