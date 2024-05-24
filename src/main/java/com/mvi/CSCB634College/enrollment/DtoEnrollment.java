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
    private Integer grade;
    private Date date;
    private Integer absences;
    private String room;
    private Boolean autumn;
    private Integer studentId;
    private Integer professorId;
    private Integer courseId;

}
