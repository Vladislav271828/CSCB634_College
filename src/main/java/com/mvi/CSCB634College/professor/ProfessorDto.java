package com.mvi.CSCB634College.professor;

import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.department.DtoDepartment;
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
public class ProfessorDto {
    private Integer id;
    private DtoDepartment department;

}
