package com.mvi.CSCB634College.course;

import com.mvi.CSCB634College.major.Major;
import com.mvi.CSCB634College.professor.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select (count(c) > 0) from Course c where c.id = ?1")
    boolean courseExists(Long id);

    List<Course> findAllByMajor(Major major);

    @Query("SELECT e.course FROM Enrollment e WHERE e.professor = :professor AND FUNCTION('YEAR', e.date) = :year")
    List<Course> findAllByProfessorAndYear(@Param("professor") Professor professor, @Param("year") int year);
}
