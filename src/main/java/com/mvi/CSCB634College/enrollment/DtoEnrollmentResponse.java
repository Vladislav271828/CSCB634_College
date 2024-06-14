package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.absence.Absence;
import com.mvi.CSCB634College.grade.Grade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoEnrollmentResponse {

    private Long id;
    private Date date;
    private String room;
    private Boolean autumn;
    private Integer studentId;
    private Integer professorId;
    private Long courseId;

    private List<Absence> absences;
    private List<Grade> grade;

}
