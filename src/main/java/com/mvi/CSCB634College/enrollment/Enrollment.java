package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.absence.Absence;
import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.enrollmentGrade.EnrollmentGrade;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "students_user_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "professors_user_id")
    private Professor professor;

    private Date date;
    private String room;
    private Boolean autumn;
    private Integer finalGrade;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Absence> absences = new ArrayList<>();

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnrollmentGrade> grades = new ArrayList<>();
}
