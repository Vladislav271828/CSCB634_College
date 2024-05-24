package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByStudent(Student student);

    List<Enrollment> findAllByCourse(Course course);


}
