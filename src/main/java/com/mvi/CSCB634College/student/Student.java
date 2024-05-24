package com.mvi.CSCB634College.student;

import com.mvi.CSCB634College.faculty.Faculty;
import com.mvi.CSCB634College.major.Major;
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
@Table(name = "students")
public class Student {

    @Id
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "faculty_faculty_id")
    private Faculty faculty;

}
