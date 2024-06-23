package com.mvi.CSCB634College.absence;


import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.enrollment.Enrollment;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    List<Absence> findAllByEnrollment(Enrollment enrollment);

    @Query("SELECT COUNT(a) FROM Absence a WHERE a.enrollment.student = :student GROUP BY a.enrollment")
    List<Long> countAbsencesByStudent(
            @Param("student") Student student);

    @Query("SELECT COUNT(a) FROM Absence a WHERE a.enrollment.professor = :professor GROUP BY a.enrollment")
    List<Long> countAbsencesByProfessor(
            @Param("professor") Professor professor);

    @Query("SELECT COUNT(a) FROM Absence a WHERE FUNCTION('YEAR', a.enrollment.date) = :year GROUP BY a.enrollment")
    List<Long> countAbsencesByYear(
            @Param("year") Integer year);

    @Query("SELECT COUNT(a) FROM Absence a WHERE a.enrollment.course = :course GROUP BY a.enrollment")
    List<Long> countAbsencesByCourse(
            @Param("course") Course course);

    @Query("SELECT COUNT(a) FROM Absence a WHERE a.enrollment.course.major = :major GROUP BY a.enrollment")
    List<Long> countAbsencesByMajor(
            @Param("major") Major major);

    @Query("SELECT COUNT(a) FROM Absence a WHERE a.enrollment.course.major.department.faculty.college = :college GROUP BY a.enrollment")
    List<Long> countAbsencesByCollege(
            @Param("college") College college);





}
