package com.mvi.CSCB634College.absence;

import com.mvi.CSCB634College.college.CollegeRepository;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.enrollment.EnrollmentRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import com.mvi.CSCB634College.student.StudentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AbsenceService {

    private final AbsenceRepository absenceRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;
    private final MajorRepository majorRepository;
    private final CollegeRepository collegeRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    private final CourseRepository courseRepository;


    public DtoAbsence createAbsence(DtoAbsence dtoAbsence) {

        Absence absence = modelMapper.map(dtoAbsence, Absence.class);

        if (dtoAbsence.getEnrollmentId() != null) {
            Enrollment enrollment = enrollmentRepository.findById(dtoAbsence.getEnrollmentId())
                    .orElseThrow(() -> new BadRequestException("Enrollment with id " + dtoAbsence.getEnrollmentId() + " not found."));
            absence.setEnrollment(enrollment);
        }
        else throw new BadRequestException("Enrollment id is required.");

        return modelMapper.map(absenceRepository.save(absence), DtoAbsence.class);
    }

    public DtoAbsence getAbsenceById(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));
        return modelMapper.map(absence, DtoAbsence.class);
    }

    public List<DtoAbsence> getAllByEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found."));

        return absenceRepository.findAllByEnrollment(enrollment).stream()
                .map(absence -> modelMapper.map(absence, DtoAbsence.class))
                .collect(Collectors.toList());
    }

    public DtoAbsence updateAbsence(Long id, DtoAbsence dtoAbsence) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));

        if (!dtoAbsence.getEnrollmentId().equals(absence.getEnrollment().getId())) {
            throw new BadRequestException("You cant change the enrollment. Create a new absence");
        }

        modelMapper.map(dtoAbsence, absence);

        return modelMapper.map(absenceRepository.save(absence), DtoAbsence.class);
    }

    public void deleteAbsence(Long id) {
        Absence absence = absenceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Absence with id " + id + " not found."));
        absenceRepository.delete(absence);
    }

    public List<Long> getNumber(String thingie, Long thingieId) {
        switch (thingie) {
            case "Student" -> {
                return absenceRepository.countAbsencesByStudent(studentRepository.findById(thingieId.intValue())
                        .orElseThrow(() -> new BadRequestException("Student with id " + thingieId + " not found")));
            }
            case "Professor" -> {
                return absenceRepository.countAbsencesByProfessor(
                        professorRepository.findById(thingieId.intValue())
                                .orElseThrow(() -> new BadRequestException("Professor with id " + thingieId + " not found"))
                );
            }
            case "Course" -> {
                return absenceRepository.countAbsencesByCourse(
                        courseRepository.findById(thingieId)
                                .orElseThrow(() -> new BadRequestException("Course with id " + thingieId + " not found"))
                );
            }
            case "Major" -> {
                return absenceRepository.countAbsencesByMajor(
                        majorRepository.findById(thingieId)
                                .orElseThrow(() -> new BadRequestException("Major with id " + thingieId + " not found"))
                );
            }
            case "College" -> {
                return absenceRepository.countAbsencesByCollege(
                        collegeRepository.findById(thingieId)
                                .orElseThrow(() -> new BadRequestException("College with id " + thingieId + " not found"))
                );
            }
            case "Year" -> {
                return absenceRepository.countAbsencesByYear(thingieId.intValue());
            }
            default -> throw new BadRequestException("You can get absences only by Student, Professor, Course, Major, College or Year");
        }
    }
}
