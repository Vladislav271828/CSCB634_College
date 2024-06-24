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

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE e.student = :student " +
            "GROUP BY e.id")
    List<Long> countAbsencesByStudent(@Param("student") Student student);

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE e.professor = :professor " +
            "GROUP BY e.id")
    List<Long> countAbsencesByProfessor(@Param("professor") Professor professor);

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE FUNCTION('YEAR', e.date) = :year " +
            "GROUP BY e.id")
    List<Long> countAbsencesByYear(@Param("year") Integer year);

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE e.course = :course " +
            "GROUP BY e.id")
    List<Long> countAbsencesByCourse(@Param("course") Course course);

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE e.course.major = :major " +
            "GROUP BY e.id")
    List<Long> countAbsencesByMajor(@Param("major") Major major);

    @Query("SELECT COALESCE(COUNT(a), 0) " +
            "FROM Enrollment e LEFT JOIN e.absences a " +
            "WHERE e.course.major.department.faculty.college = :college " +
            "GROUP BY e.id")
    List<Long> countAbsencesByCollege(@Param("college") College college);





}
