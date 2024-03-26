package com.mvi.CSCB634College.ProfessorQualification;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.professor.Professor;
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
@Table(name = "professor_qualification")
public class ProfessorQualification {
    @Id
    @ManyToOne
    @JoinColumn(name = "courses_course_id")
    private Course course;

    @Id
    @ManyToOne
    @JoinColumn(name = "professors_user_id")
    private Professor professor;

}
