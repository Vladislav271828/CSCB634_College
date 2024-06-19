package com.mvi.CSCB634College.program;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.professor.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "program", uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "professors_user_id", "year", "autumn", "education_year"}))
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    private Integer year;

    private Boolean autumn;

    @Column(name = "education_year")
    private Integer educationYear;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Professor> professors;

}
