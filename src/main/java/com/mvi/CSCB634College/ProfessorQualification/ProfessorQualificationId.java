package com.mvi.CSCB634College.ProfessorQualification;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProfessorQualificationId implements Serializable {
    private Long course;
    private Integer professor;

    // Default constructor
    public ProfessorQualificationId() {}

    // Parameterized constructor
    public ProfessorQualificationId(Long course, Integer professor) {
        this.course = course;
        this.professor = professor;
    }

    // Getters, setters, equals, and hashCode methods
}

