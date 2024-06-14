package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByStudent(Student student);

    List<Enrollment> findAllByCourse(Course course);

    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND FUNCTION('YEAR', e.date) = :year")
    List<Enrollment> findAllByStudentAndYear(@Param("student") Student student, @Param("year") int year);




}
