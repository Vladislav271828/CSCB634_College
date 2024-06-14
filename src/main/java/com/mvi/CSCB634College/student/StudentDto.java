package com.mvi.CSCB634College.student;

import com.mvi.CSCB634College.major.DtoMajor;
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
public class StudentDto {

    private Integer id;

    private DtoMajor major;


}
