package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.faculty.Faculty;
import com.mvi.CSCB634College.professor.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "departments", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "college_college_id"})})
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "department")
    private Set<Professor> professors;

    @ManyToOne
    @JoinColumn(name = "department_head_user_id")
    private Professor departmentHead;

}