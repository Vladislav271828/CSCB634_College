package com.mvi.CSCB634College.faculty;

import com.mvi.CSCB634College.college.College;
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
@Table(name = "faculty")
public class Faculty {
    @Id
    private Long faculty_id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "college_college_id")
    private College college;

}
