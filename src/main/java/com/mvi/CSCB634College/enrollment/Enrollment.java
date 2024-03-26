package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.student.Student;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Id
    @ManyToOne
    @JoinColumn(name = "students_user_id")
    private Student student;

    private Integer grade;
    private Date date;
    private Integer absences;

}
