package com.mvi.CSCB634College.grade;

import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.enrollment.EnrollmentRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GradeService(GradeRepository gradeRepository, EnrollmentRepository enrollmentRepository, ModelMapper modelMapper) {
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.modelMapper = modelMapper;
    }

    public DtoGrade createGrade(DtoGrade dtoGrade) {

        Grade grade = modelMapper.map(dtoGrade, Grade.class);

      if (dtoGrade.getEnrollmentId() != null) {
          Enrollment enrollment = enrollmentRepository.findById(dtoGrade.getEnrollmentId())
                  .orElseThrow(() -> new BadRequestException("Enrollment with id " + dtoGrade.getEnrollmentId() + " not found."));
          grade.setEnrollment(enrollment);
      }
      else throw new BadRequestException("Enrollment id is required.");

      return modelMapper.map(gradeRepository.save(grade), DtoGrade.class);
    }

    public DtoGrade getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Grade with id " + id + " not found."));
        return modelMapper.map(grade, DtoGrade.class);
    }

    public List<DtoGrade> getAllByEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BadRequestException("Enrollment with id " + enrollmentId + " not found."));
        
        return gradeRepository.findAllByEnrollment(enrollment).stream()
                .map(grade -> modelMapper.map(grade, DtoGrade.class))
                .collect(Collectors.toList());
    }

    public DtoGrade updateGrade(Long id, DtoGrade dtoGrade) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Grade with id " + id + " not found."));

        if (!dtoGrade.getEnrollmentId().equals(grade.getEnrollment().getId())) {
            throw new BadRequestException("You cant change the enrollment. Create a new grade");
        }

        modelMapper.map(dtoGrade, grade);

        return modelMapper.map(gradeRepository.save(grade), DtoGrade.class);
    }

    public void deleteGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Grade with id " + id + " not found."));
        gradeRepository.delete(grade);
    }




}
    