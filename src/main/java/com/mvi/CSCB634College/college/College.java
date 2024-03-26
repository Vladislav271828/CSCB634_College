package com.mvi.CSCB634College.college;

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
@Table(name = "college")
public class College {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "college_id")
    private Long id;

    private String name;
    private String address;

    @ManyToOne
    @JoinColumn(name = "rector_id")
    private User rector;

}
