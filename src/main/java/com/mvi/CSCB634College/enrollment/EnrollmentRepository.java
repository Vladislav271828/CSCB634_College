package com.mvi.CSCB634College.enrollment;

import com.mvi.CSCB634College.college.College;
import com.mvi.CSCB634College.course.Course;
import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.professor.Professor;
import com.mvi.CSCB634College.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    @Query("SELECT e FROM Enrollment e WHERE e.student = :student AND FUNCTION('YEAR', e.date) = :year")
    List<Enrollment> findAllByStudentAndYear(
            @Param("student") Student student,
            @Param("year") int year);

    @Query("SELECT e FROM Enrollment e WHERE e.professor = :professor AND FUNCTION('YEAR', e.date) = :year AND e.course = :course")
    List<Enrollment> findAllByProfessorAndYearAndCourse(
            @Param("professor") Professor professor,
            @Param("year") int year,
            @Param("course") Course course);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE e.student = :student")
    List<Integer> findFinalGradesByStudent(
            @Param("student") Student student);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE e.professor = :professor")
    List<Integer> findFinalGradesByProfessor(
            @Param("professor") Professor professor);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE FUNCTION('YEAR', e.date) = :year")
    List<Integer> findFinalGradesByYear(
            @Param("year") Integer year);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE e.course = :course")
    List<Integer> findFinalGradesByCourse(
            @Param("course") Course course);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE e.course.major = :major")
    List<Integer> findFinalGradesByMajor(
            @Param("major") Major major);

    @Query("SELECT e.finalGrade FROM Enrollment e WHERE e.course.major.department.faculty.college = :college")
    List<Integer> findFinalGradesByCollege(
            @Param("college") College college);
}
