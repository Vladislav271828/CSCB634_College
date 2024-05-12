package com.mvi.CSCB634College.course;

import com.mvi.CSCB634College.exception.BadRequestException;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.major.MajorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final MajorRepository majorRepository;

    public CourseService(CourseRepository courseRepository, ModelMapper modelMapper, MajorRepository majorRepository) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.majorRepository = majorRepository;
    }

    public DtoCourse createCourse(DtoCourse dtoCourse, int majorId) {
        dtoCourse.setMajorId(null);

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new BadRequestException("Major with id " + majorId + " not found"));

        Course course = modelMapper.map(dtoCourse, Course.class);
        course.setMajor(major);

        return modelMapper.map(courseRepository.save(course), DtoCourse.class);
    }

    public DtoCourse getCourseById(Integer id) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Course with id " + id + " not found."));

        return modelMapper.map(course, DtoCourse.class);
    }

    public List<DtoCourse> getAllByMajor(Integer majorId) {

        Major major = majorRepository.findById(majorId)
                .orElseThrow(() -> new BadRequestException("Major with id " + majorId + " not found"));

        List<Course> courses = courseRepository.findAllByMajor(major);

        return courses.stream()
                .map(course -> modelMapper.map(course, DtoCourse.class))
                .collect(Collectors.toList());

    }

    public DtoCourse updateCourse(Integer id, DtoCourse dtoCourse) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Course with id " + id + " not found."));
        //trqbwat proverki
        modelMapper.map(dtoCourse, course);

        return modelMapper.map(courseRepository.save(course), DtoCourse.class);
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
