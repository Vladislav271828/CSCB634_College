package com.mvi.CSCB634College.ProfessorQualification;


import com.mvi.CSCB634College.course.DtoCourse;
import com.mvi.CSCB634College.professor.ProfessorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfessorQualificationResponse {

    private Integer professorQualificationId;

    private DtoCourse course;

    private ProfessorDto professor;



}
