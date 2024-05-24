package com.mvi.CSCB634College.professor;

import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.user.User;
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
@Table(name = "professors")
public class Professor {
    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;

    // Getters and setters
}
