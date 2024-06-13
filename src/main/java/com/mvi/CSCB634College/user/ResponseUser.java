package com.mvi.CSCB634College.user;

import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseUser {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;
    //TODO return data for professor and student/ returns null if not
//    private ProfessorDto professor;
//    private StudentDto student;

}
