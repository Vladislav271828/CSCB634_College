package com.mvi.CSCB634College.enrollmentGrade;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentGradeId implements Serializable {
    private Long enrollmentId;
    private Long gradeId;

}
