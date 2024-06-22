package com.mvi.CSCB634College.grade;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.course.CourseRepository;
import com.mvi.CSCB634College.exception.BadRequestException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;


    public DtoGrade createGrade(DtoGrade dtoGrade) {

        Grade grade = modelMapper.map(dtoGrade, Grade.class);

      if (dtoGrade.getCourseId() != null) {
          Course course = courseRepository.findById(dtoGrade.getCourseId())
                  .orElseThrow(() -> new BadRequestException("Course with id " + dtoGrade.getCourseId() + " not found."));
          grade.setCourse(course);
      }
      else throw new BadRequestException("Course id is required.");

      return modelMapper.map(gradeRepository.save(grade), DtoGrade.class);
    }

    public DtoGrade getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Grade with id " + id + " not found."));
        return modelMapper.map(grade, DtoGrade.class);
    }

    public List<DtoGrade> getAllByCourseAndYear(Long courseId, Integer year) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadRequestException("Course with id " + courseId + " not found."));

        return gradeRepository.findAllByCourseAndYear(course, year).stream()
                .map(grade -> modelMapper.map(grade, DtoGrade.class))
                .collect(Collectors.toList());
    }


    public DtoGrade updateGrade(Long id, DtoGrade dtoGrade) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Grade with id " + id + " not found."));

        if (dtoGrade.getCourseId() != null && (!dtoGrade.getCourseId().equals(grade.getCourse().getId()))) {
            throw new BadRequestException("You cant change the course. Create a new grade for that course");
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
    