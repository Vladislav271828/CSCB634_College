package com.mvi.CSCB634College.student;

import com.mvi.CSCB634College.department.DtoDepartment;
import com.mvi.CSCB634College.major.DtoMajor;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorDto;
import com.mvi.CSCB634College.user.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;


    public List<ResponseUser> getAllStudentsByMajorId(Long majorId) {
        List<Student> students = studentRepository.findByMajor_Id(majorId);

        return getResponseUsers(students);
    }



    private List<ResponseUser> getResponseUsers(List<Student> students) {
        List<ResponseUser> responseUsers = new ArrayList<>();
        for (Student student: students) {
            ResponseUser responseUser = new ResponseUser();
            responseUser.setId(student.getUser().getId());
            responseUser.setEmail(student.getUser().getEmail());
            responseUser.setFirstname(student.getUser().getFirstname());
            responseUser.setLastname(student.getUser().getLastname());
            responseUser.setRole(student.getUser().getRole());
            responseUser.setProfessor(new ProfessorDto());

            DtoMajor dtoMajor = new DtoMajor();
            dtoMajor.setName(student.getMajor().getName());
            dtoMajor.setDepartmentId(student.getMajor().getDepartment().getId());
            dtoMajor.setId(student.getMajor().getId());

            StudentDto studentDto = new StudentDto();
            studentDto.setMajor(dtoMajor);
            studentDto.setId(student.getId());

            responseUser.setStudent(studentDto);


            responseUsers.add(responseUser);
        }

        return responseUsers;
    }
}
