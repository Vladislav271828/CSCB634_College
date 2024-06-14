package com.mvi.CSCB634College.absence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvi.CSCB634College.enrollment.Enrollment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "absence")
public class Absence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id")
    @JsonIgnore
    private Enrollment enrollment;
    @NotNull(message = "Date is required.")
    private Date date;
    private String note;



}
