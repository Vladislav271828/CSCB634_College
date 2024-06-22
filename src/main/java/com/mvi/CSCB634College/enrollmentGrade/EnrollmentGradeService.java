package com.mvi.CSCB634College.enrollmentGrade;

import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.enrollment.EnrollmentRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.grade.Grade;
import com.mvi.CSCB634College.grade.GradeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentGradeService {

    private final ModelMapper modelMapper;
    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentGradeRepository enrollmentGradeRepository;

    public DtoEnrollmentGradeResponse createEnrollmentGrade(DtoEnrollmentGrade dtoEnrollmentGrade) {

        Enrollment enrollment = enrollmentRepository.findById(dtoEnrollmentGrade.getEnrollmentId())
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + dtoEnrollmentGrade.getEnrollmentId() + " not found."));


        Grade grade = gradeRepository.findById(dtoEnrollmentGrade.getGradeId())
                .orElseThrow(() -> new BadRequestException("Grade with id " + dtoEnrollmentGrade.getGradeId() + " not found."));

        EnrollmentGrade enrollmentGrade = modelMapper.map(dtoEnrollmentGrade, EnrollmentGrade.class);

        return modelMapper.map(enrollmentGradeRepository.save(enrollmentGrade), DtoEnrollmentGradeResponse.class);
    }

    public DtoEnrollmentGradeResponse updateEnrollmentGrade(Long id, DtoEnrollmentGrade dtoEnrollmentGrade) {

        EnrollmentGrade enrollmentGrade = enrollmentGradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("EnrollmentGrade with id " + id + " not found."));

        enrollmentGrade.setScore(dtoEnrollmentGrade.getScore());

        return modelMapper.map(enrollmentGradeRepository.save(enrollmentGrade), DtoEnrollmentGradeResponse.class);
    }

//    public DtoEnrollmentGradeResponse getEnrollmentGradeById(Long id) {
//        EnrollmentGrade enrollmentGrade = enrollmentGradeRepository.findById(id)
//                .orElseThrow(() -> new BadRequestException("EnrollmentGrade with id " + id + " not found."));
//
//        return modelMapper.map(enrollmentGrade, DtoEnrollmentGradeResponse.class);
//    }

    public void deleteEnrollmentGrade(Long id) {
        EnrollmentGrade enrollmentGrade = enrollmentGradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("EnrollmentGrade with id " + id + " not found."));

        enrollmentGradeRepository.delete(enrollmentGrade);
    }
}
