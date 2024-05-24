package com.mvi.CSCB634College.course;

import com.mvi.CSCB634College.major.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByMajor(Major major);
}
