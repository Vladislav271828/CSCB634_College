package com.mvi.CSCB634College.enrollment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DtoEnrollment {

    private Long id;
    private Date date;
    private String room;
    private Boolean autumn;
    private Integer studentId;
    private Integer professorId;
    private Long courseId;
    private Integer finalGrade;

}
