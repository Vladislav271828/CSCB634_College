package com.mvi.CSCB634College.grade;

import com.mvi.CSCB634College.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByCourseAndYear(Course course, Integer year);
}
