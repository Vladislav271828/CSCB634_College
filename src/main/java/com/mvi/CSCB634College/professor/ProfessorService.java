package com.mvi.CSCB634College.professor;


import com.mvi.CSCB634College.department.DtoDepartment;
import com.mvi.CSCB634College.user.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    public List<ResponseUser> getAllProfessorsByCollegeId(Long collegeId) {

        List<Professor> professors = professorRepository.findByDepartment_College_Id(collegeId);

        return getResponseUsers(professors);
    }



    public List<ResponseUser> getAllProfessorsByDepartmentId(Long departmentId) {


        List<Professor> professors = professorRepository.findByDepartment_Id(departmentId);

        return getResponseUsers(professors);
    }

    private List<ResponseUser> getResponseUsers(List<Professor> professors) {
        List<ResponseUser> responseUsers = new ArrayList<>();
        for (Professor professor: professors) {
            ResponseUser responseUser = new ResponseUser();
            responseUser.setId(professor.getId());
            responseUser.setEmail(professor.getUser().getEmail());
            responseUser.setFirstname(professor.getUser().getFirstname());
            responseUser.setLastname(professor.getUser().getLastname());
            responseUser.setRole(professor.getUser().getRole());
            responseUser.setStudent(null);

            ProfessorDto professorDto = getProfessorDto(professor);

            responseUser.setProfessor(professorDto);

            responseUsers.add(responseUser);
        }

        return responseUsers;
    }

    private static ProfessorDto getProfessorDto(Professor professor) {
        ProfessorDto professorDto = new ProfessorDto();
        professorDto.setId(professor.getId());

        DtoDepartment dtoDepartment = new DtoDepartment();
        dtoDepartment.setFacultyId(professor.getDepartment().getFaculty().getId());
        dtoDepartment.setName(professor.getDepartment().getName());

        Integer departmentHadId;

        if (professor.getDepartment().getDepartmentHead() != null){
            departmentHadId = professor.getDepartment().getDepartmentHead().getId();
        }else{
            departmentHadId = null;
        }

        dtoDepartment.setDepartmentHeadId(departmentHadId);

        professorDto.setDepartment(dtoDepartment);
        return professorDto;
    }
}
