package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.student.Student;
import com.mvi.CSCB634College.student.StudentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final StudentRepository studentRepository;

    private final ProfessorRepository professorRepository;

    private final CourseRepository courseRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public EnrollmentService(EnrollmentRepository enrollmentRepository, StudentRepository studentRepository, ProfessorRepository professorRepository, CourseRepository courseRepository, ModelMapper modelMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.professorRepository = professorRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
    }

    public DtoEnrollment createEnrollment(DtoEnrollment dtoEnrollment){

        Student student = studentRepository.findById(dtoEnrollment.getStudentId())
                .orElseThrow(() -> new BadRequestException("Student with id " + dtoEnrollment.getProfessorId() + " not found"));

        Professor professor = professorRepository.findById(dtoEnrollment.getProfessorId())
                .orElseThrow(() -> new BadRequestException("Professor with id " + dtoEnrollment.getProfessorId() + " not found"));

        Course course = courseRepository.findById(dtoEnrollment.getCourseId())
                .orElseThrow(() -> new BadRequestException("Course with id " + dtoEnrollment.getCourseId() + " not found"));

        Enrollment enrollment = modelMapper.map(dtoEnrollment, Enrollment.class);
        enrollment.setStudent(student);
        enrollment.setProfessor(professor);
        enrollment.setCourse(course);
        return modelMapper.map(enrollmentRepository.save(enrollment), DtoEnrollment.class);
    }

    public DtoEnrollment getEnrollmentById(Long enrollmentId){
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));

        return modelMapper.map(enrollment, DtoEnrollment.class);
    }

    public List<DtoEnrollment> getAllEnrollmentByStudentId(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BadRequestException("Student with id " + studentId + " not found"));

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudent(student);

        return enrollments.stream()
                .map(Enrollment -> modelMapper.map(Enrollment, DtoEnrollment.class))
                .collect(Collectors.toList());
    }

    public DtoEnrollment updateEnrollment(Long enrollmentId, DtoEnrollment dtoEnrollment){

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));
        modelMapper.map(dtoEnrollment, enrollment);

        if (!Objects.equals(enrollment.getStudent().getId(), dtoEnrollment.getStudentId())) {
            Student student = studentRepository.findById(dtoEnrollment.getStudentId())
                    .orElseThrow(() -> new BadRequestException("Student with id " + dtoEnrollment.getProfessorId() + " not found"));

            enrollment.setStudent(student);
        }
        if (!Objects.equals(enrollment.getProfessor().getId(), dtoEnrollment.getProfessorId())) {
            Professor professor = professorRepository.findById(dtoEnrollment.getProfessorId())
                    .orElseThrow(() -> new BadRequestException("Professor with id " + dtoEnrollment.getProfessorId() + " not found"));

            enrollment.setProfessor(professor);
        }
        if (!Objects.equals(enrollment.getCourse().getId(), dtoEnrollment.getCourseId())) {
            Course course = courseRepository.findById(dtoEnrollment.getCourseId())
                    .orElseThrow(() -> new BadRequestException("Course with id " + dtoEnrollment.getCourseId() + " not found"));

            enrollment.setCourse(course);
        }

        return modelMapper.map(enrollmentRepository.save(enrollment), DtoEnrollment.class);
    }

    public void deleteEnrollment(Long enrollmentId){
    enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));
        enrollmentRepository.deleteById(enrollmentId);
    }





    



}





