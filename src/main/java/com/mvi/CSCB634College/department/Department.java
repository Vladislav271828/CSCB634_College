package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.professor.Professor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

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
    @JoinColumn(name = "college_college_id", nullable = false)
    private College college;

    @ManyToOne
    @JoinColumn(name = "department_head_user_id")
    private Professor departmentHead;

}