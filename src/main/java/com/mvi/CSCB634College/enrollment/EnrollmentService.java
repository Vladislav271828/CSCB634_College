package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.ProfessorQualification.ProfessorQualificationRepository;
import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.exception.CourseNotFoundException;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.student.Student;
import com.mvi.CSCB634College.student.StudentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final StudentRepository studentRepository;

    private final ProfessorRepository professorRepository;

    private final CourseRepository courseRepository;

    private final ModelMapper modelMapper;
    private final ProfessorQualificationRepository professorQualificationRepository;
    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;


    public DtoEnrollmentResponse createEnrollment(DtoEnrollment dtoEnrollment) {

        Student student = studentRepository.findById(dtoEnrollment.getStudentId())
                .orElseThrow(() -> new BadRequestException("Student with id " + dtoEnrollment.getProfessorId() + " not found"));

        Professor professor = professorRepository.findById(dtoEnrollment.getProfessorId())
                .orElseThrow(() -> new BadRequestException("Professor with id " + dtoEnrollment.getProfessorId() + " not found"));


        Course course = courseRepository.findById(dtoEnrollment.getCourseId())
                .orElseThrow(() -> new BadRequestException("Course with id " + dtoEnrollment.getCourseId() + " not found"));

        if (!Objects.equals(student.getMajor().getId(), course.getMajor().getId())){
            throw new BadRequestException("Student is of major " + student.getMajor().getId() + " and can't join course of major " + course.getMajor().getId() + ".");
        }

        if (!professorQualificationRepository.existsByCourse_IdAndProfessor_Id(dtoEnrollment.getCourseId(), professor.getId())) {
            throw new BadRequestException("Professor with id " + professor.getId() + "(" + professor.getUser().getEmail() + ") doesn't have entry in professor qualification for course with id " + dtoEnrollment.getCourseId() + "(" + course.getName() + ").");
        }

        Enrollment enrollment = modelMapper.map(dtoEnrollment, Enrollment.class);
        enrollment.setStudent(student);
        enrollment.setProfessor(professor);
        enrollment.setCourse(course);
        return modelMapper.map(enrollmentRepository.save(enrollment), DtoEnrollmentResponse.class);
    }

    public DtoEnrollmentResponse getEnrollmentById(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));

        return modelMapper.map(enrollment, DtoEnrollmentResponse.class);
    }

    public List<DtoEnrollmentResponse> getAllEnrollmentByStudentIdAndYear(Integer studentId, Integer year) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new BadRequestException("Student with id " + studentId + " not found"));

        List<Enrollment> enrollments = enrollmentRepository.findAllByStudentAndYear(student, year);

        return enrollments.stream()
                .map(Enrollment -> modelMapper.map(Enrollment, DtoEnrollmentResponse.class))
                .collect(Collectors.toList());
    }

    public List<DtoEnrollmentResponse> getAllEnrollmentByProfessorIdAndYearAndCourse(Integer professorId, Integer year, Long courseId) {
        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new BadRequestException("Professor with id " + professorId + " not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadRequestException("Course with id " + courseId + " not found"));

        List<Enrollment> enrollments = enrollmentRepository.findAllByProfessorAndYearAndCourse(professor, year, course);

        return enrollments.stream()
                .map(Enrollment -> modelMapper.map(Enrollment, DtoEnrollmentResponse.class))
                .collect(Collectors.toList());
    }

    public DtoEnrollmentResponse updateEnrollment(Long enrollmentId, DtoEnrollment dtoEnrollment) {

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));
        modelMapper.map(dtoEnrollment, enrollment);

        if (!Objects.equals(enrollment.getStudent().getId(), dtoEnrollment.getStudentId())) {
            Student student = studentRepository.findById(dtoEnrollment.getStudentId())
                    .orElseThrow(() -> new BadRequestException("Student with id " + dtoEnrollment.getProfessorId() + " not found"));


            if (!courseRepository.existsById(dtoEnrollment.getCourseId())){
                throw new CourseNotFoundException("Course with id " + dtoEnrollment.getCourseId() + " doesn't exist.");
            }

            Course course = courseRepository.findById(dtoEnrollment.getCourseId()).orElseThrow();

            if (!Objects.equals(student.getMajor().getId(), course.getMajor().getId())){
                throw new BadRequestException("Student is of major " + student.getMajor().getId() + " and can't join course of major " + course.getMajor().getId() + ".");
            }

            enrollment.setStudent(student);
        }
        if (!Objects.equals(enrollment.getProfessor().getId(), dtoEnrollment.getProfessorId())) {
            Professor professor = professorRepository.findById(dtoEnrollment.getProfessorId())
                    .orElseThrow(() -> new BadRequestException("Professor with id " + dtoEnrollment.getProfessorId() + " not found"));

            if (!professorQualificationRepository.existsByCourse_IdAndProfessor_Id(dtoEnrollment.getCourseId(), professor.getId())) {
                throw new BadRequestException("Professor with id " + professor.getId() + "(" + professor.getUser().getEmail() + ") doesn't have entry in professor qualification for course with id " + dtoEnrollment.getCourseId() + ".");
            }


            enrollment.setProfessor(professor);
        }
        if (!Objects.equals(enrollment.getCourse().getId(), dtoEnrollment.getCourseId())) {
            Course course = courseRepository.findById(dtoEnrollment.getCourseId())
                    .orElseThrow(() -> new BadRequestException("Course with id " + dtoEnrollment.getCourseId() + " not found"));

            if (!professorQualificationRepository.existsByCourse_IdAndProfessor_Id(dtoEnrollment.getCourseId(), dtoEnrollment.getProfessorId())) {
                throw new BadRequestException("Professor with id " + dtoEnrollment.getId() + " doesn't have entry in professor qualification for course with id " + dtoEnrollment.getCourseId() + ".");
            }

            enrollment.setCourse(course);
        }

        return modelMapper.map(enrollmentRepository.save(enrollment), DtoEnrollmentResponse.class);
    }

    public void deleteEnrollment(Long enrollmentId) {
        enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found"));
        enrollmentRepository.deleteById(enrollmentId);
    }

    public List<Integer> getGrades(String thingie, Long thingieId){
            switch (thingie) {
                case "Student" -> {
                    return enrollmentRepository.findFinalGradesByStudent(studentRepository.findById(thingieId.intValue())
                            .orElseThrow(() -> new BadRequestException("Student with id " + thingieId + " not found")));
                }
                case "Professor" -> {
                    return enrollmentRepository.findFinalGradesByProfessor(
                            professorRepository.findById(thingieId.intValue())
                                    .orElseThrow(() -> new BadRequestException("Professor with id " + thingieId + " not found"))
                    );
                }
                case "Course" -> {
                    return enrollmentRepository.findFinalGradesByCourse(
                            courseRepository.findById(thingieId)
                                    .orElseThrow(() -> new BadRequestException("Course with id " + thingieId + " not found"))
                    );
                }
                case "Major" -> {
                    return enrollmentRepository.findFinalGradesByMajor(
                            majorRepository.findById(thingieId)
                                    .orElseThrow(() -> new BadRequestException("Major with id " + thingieId + " not found"))
                    );
                }
                case "College" -> {
                    return enrollmentRepository.findFinalGradesByCollege(
                            collegeRepository.findById(thingieId)
                                    .orElseThrow(() -> new BadRequestException("College with id " + thingieId + " not found"))
                    );
                }
                case "Year" -> {
                    return enrollmentRepository.findFinalGradesByYear(thingieId.intValue());
                }
                default -> throw new BadRequestException("You can get grades only by Student, Professor, Course, Major, College or Year");
        }














    }


}






