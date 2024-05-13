package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.professor.Professor;
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


    private Integer grade; //posle she go naprawim otdelna tablica da ima mnogo ocenki
    private Date date;
    private Integer absences; //posle she go naprawim otdelna tablica da ima mnogo otsustwiq
    private String room;




    //tuka shte trqbwa custom zaqwki, demek edna da pokazwa wsichkite grupi naprimer
}
