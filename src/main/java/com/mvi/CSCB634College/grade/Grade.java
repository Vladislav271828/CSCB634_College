package com.mvi.CSCB634College.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvi.CSCB634College.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Integer year;

}

