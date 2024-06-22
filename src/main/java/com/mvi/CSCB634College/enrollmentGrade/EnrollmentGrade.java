package com.mvi.CSCB634College.enrollmentGrade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.grade.Grade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollment_grade")
public class EnrollmentGrade {

    @EmbeddedId
    private EnrollmentGradeId id;

    @ManyToOne
    @MapsId("enrollmentId")
    @JoinColumn(name = "enrollment_id")
    @JsonIgnore
    private Enrollment enrollment;

    @ManyToOne
    @MapsId("gradeId")
    @JoinColumn(name = "grade_id")
    private Grade grade;

    private Integer score;
}