package com.mvi.CSCB634College.course;

import com.mvi.CSCB634College.major.Major;
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
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name")
    private String name;

    @Column(name = "in_program")
    private boolean inProgram;

    @ManyToOne
    @JoinColumn(name = "professors_user_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    // Getters and setters
}