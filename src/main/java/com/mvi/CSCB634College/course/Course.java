package com.mvi.CSCB634College.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvi.CSCB634College.major.Major;
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

    private String signature;

    private String description;

    private Integer credits;

    @ManyToOne
    @JoinColumn(name = "major_id")
    @JsonIgnore
    private Major major;

    // Getters and setters
}