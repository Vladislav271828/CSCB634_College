package com.mvi.CSCB634College.course;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.major.MajorRepository;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.professor.ProfessorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final MajorRepository majorRepository;
    private final ProfessorRepository professorRepository;

    public CourseService(CourseRepository courseRepository, ModelMapper modelMapper, MajorRepository majorRepository, ProfessorRepository professorRepository) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.majorRepository = majorRepository;
        this.professorRepository = professorRepository;
    }

    public DtoCourse createCourse(DtoCourse dtoCourse) {

        Course course = modelMapper.map(dtoCourse, Course.class);
        if(dtoCourse.getMajorId() != null){
        Major major = majorRepository.findById(dtoCourse.getMajorId())
                .orElseThrow(() -> new BadRequestException("Major with id " + dtoCourse.getMajorId() + " not found"));
        course.setMajor(major);
        }

        return modelMapper.map(courseRepository.save(course), DtoCourse.class);
    }

    public DtoCourse getCourseById(Long id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Course with id " + id + " not found."));

        return modelMapper.map(course, DtoCourse.class);
    }

    public List<DtoCourse> getAllByMajor(Long majorId) {

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new BadRequestException("Major with id " + majorId + " not found"));

        List<Course> courses = courseRepository.findAllByMajor(major);

        return courses.stream()
                .map(course -> modelMapper.map(course, DtoCourse.class))
                .collect(Collectors.toList());
    }

    public List<DtoCourse> getAllByProfessorAndYear(Integer professorId, Integer year) {

        Professor professor = professorRepository.findById(professorId)
                .orElseThrow(() -> new BadRequestException("Professor with id " + professorId + " not found"));

        List<Course> courses = courseRepository.findAllByProfessorAndYear(professor, year);

        return courses.stream()
                .map(course -> modelMapper.map(course, DtoCourse.class))
                .collect(Collectors.toList());

    }

    public DtoCourse updateCourse(Long id, DtoCourse dtoCourse) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Course with id " + id + " not found."));


        if(dtoCourse.getMajorId() != null && !dtoCourse.getMajorId().equals(course.getMajor().getId())){
            Major major = majorRepository.findById(dtoCourse.getMajorId())
                    .orElseThrow(() -> new BadRequestException("Major with id " + dtoCourse.getMajorId() + " not found"));
            course.setMajor(major);
        }

        modelMapper.map(dtoCourse, course);

        return modelMapper.map(courseRepository.save(course), DtoCourse.class);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Course with id " + id + " not found."));

        courseRepository.delete(course);
    }
}
