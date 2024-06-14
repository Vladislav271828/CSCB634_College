package com.mvi.CSCB634College.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvi.CSCB634College.enrollment.Enrollment;
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
@Table(name = "grade")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    @JsonIgnore
    private Enrollment enrollment;

    private String name;
    private Double grade;

}

