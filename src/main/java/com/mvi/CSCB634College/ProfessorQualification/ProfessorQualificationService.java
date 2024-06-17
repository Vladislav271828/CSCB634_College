package com.mvi.CSCB634College.ProfessorQualification;


import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.course.DtoCourse;
import com.mvi.CSCB634College.department.Department;
import com.mvi.CSCB634College.department.DepartmentRepository;
import com.mvi.CSCB634College.department.DtoDepartment;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.CourseNotFoundException;
import com.mvi.CSCB634College.exception.ProfessorQualificationNotFound;
import com.mvi.CSCB634College.exception.UserNotFound;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorDto;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorQualificationService {

    private final ProfessorRepository professorRepository;
    private final CourseRepository courseRepository;
    private final ProfessorQualificationRepository professorQualificationRepository;
    private final DepartmentRepository departmentRepository;

    public ProfessorQualificationResponse createProfessorQualification(ProfessorQualificationRequest request) {
        ProfessorQualification professorQualification = mapProfessorQualificationDtoToObjectAndSave(request);

        return mapProfessorQualificationDtoToResponse(professorQualification);
    }

    public ProfessorQualificationResponse updateProfessorQualification(ProfessorQualificationRequest request) {

        ProfessorQualification professorQualification = mapProfessorQualificationDtoToObjectAndSave(request);

        return mapProfessorQualificationDtoToResponse(professorQualification);
    }


    public List<ProfessorQualificationResponse> getProfessorQualificationByProfessorId(Integer professorId) {
        if (!professorQualificationRepository.existsByProfessor_Id(professorId)){
            throw new ProfessorQualificationNotFound("No entries found in professor qualifications for professor with id " + professorId + ".");
        }

        List<ProfessorQualification> qualifications = professorQualificationRepository.findByProfessor_Id(professorId);

        return mapCollectionToResponseEntitiesForProfessorQualification(qualifications);

    }

    private List<ProfessorQualificationResponse> mapCollectionToResponseEntitiesForProfessorQualification(List<ProfessorQualification> qualifications) {
        List<ProfessorQualificationResponse> responses = new ArrayList<>();
        for (ProfessorQualification professorQualification:qualifications) {
            ProfessorQualificationResponse response = mapProfessorQualificationDtoToResponse(professorQualification);

            responses.add(response);
        }

        return responses;
    }

    public void deleteProfessorQualification(ProfessorQualificationRequest request) {
        if (professorQualificationRepository.existsByCourse_IdAndProfessor_Id(request.getCourseId().longValue(), request.getProfessorId())){
            throw new ProfessorQualificationNotFound("Professor qualification for professor with id " + request.getProfessorId() + " and course " + request.getCourseId() + " doesn't exists.");
        }

        ProfessorQualification professorQualification = professorQualificationRepository.findByCourse_IdAndProfessor_Id(request.getCourseId().longValue(), request.getProfessorId()).orElseThrow();

        professorQualificationRepository.delete(professorQualification);

    }

    private ProfessorQualificationResponse mapProfessorQualificationDtoToResponse(ProfessorQualification professorQualification) {
        ProfessorQualificationResponse response = new ProfessorQualificationResponse();

        Department department = departmentRepository.findByProfessors_Id(professorQualification.getProfessor().getId()).orElseThrow();
        DtoDepartment dtoDepartment = new DtoDepartment();
        if (department.getDepartmentHead() != null) {
            dtoDepartment.setDepartmentHeadId(department.getDepartmentHead().getId());
        } else {
            dtoDepartment.setDepartmentHeadId(null);
        }
        ProfessorDto professorDto = new ProfessorDto();
        dtoDepartment.setName(department.getName());
        dtoDepartment.setFacultyId(department.getFaculty().getId());
        dtoDepartment.setId(department.getId());
        professorDto.setDepartment(dtoDepartment);


        Professor professor = professorRepository.findProfessorByUserId(professorQualification.getProfessor().getId()).orElseThrow();
        professorDto.setId(professor.getId());
        response.setProfessor(professorDto);


        Course course = courseRepository.findById(professorQualification.getCourse().getId()).orElseThrow();
        DtoCourse dtoCourse = new DtoCourse();
        dtoCourse.setName(course.getName());
        dtoCourse.setId(course.getId());
        dtoCourse.setCredits(course.getCredits());
        dtoCourse.setSignature(course.getSignature());
        dtoCourse.setMajorId(course.getMajor().getId());
        dtoCourse.setDescription(course.getDescription());


        response.setCourse(dtoCourse);
        return response;
    }


    private ProfessorQualification mapProfessorQualificationDtoToObjectAndSave(ProfessorQualificationRequest dto) {
        //check if the course exists
        if (!courseRepository.courseExists(dto.getCourseId().longValue())) {
            throw new CourseNotFoundException("Course with id " + dto.getCourseId() + " doesn't exist.");
        }

        //check if the professor exists
        if (!professorRepository.existsById(dto.getProfessorId())) {
            throw new UserNotFound("Professor with id " + dto.getProfessorId() + " doesn't exist.");
        }


        Professor professor = professorRepository.findProfessorByUserId(dto.getProfessorId()).orElseThrow();
        Course course = courseRepository.findById(dto.getCourseId().longValue()).orElseThrow();

        if (professorQualificationRepository.existsByCourse_IdAndProfessor_Id(dto.getCourseId().longValue(), dto.getProfessorId())) {
            throw new BadRequestException("Professor with id " + dto.getProfessorId() + " (" + professor.getUser().getEmail() + ") already has an entry for course with id " + dto.getCourseId() + " (" + course.getName() + ").");
        }


        ProfessorQualification professorQualification = new ProfessorQualification();
        professorQualification.setProfessor(professor);
        professorQualification.setCourse(course);

        professorQualification = professorQualificationRepository.save(professorQualification);

        return professorQualification;
    }
}
