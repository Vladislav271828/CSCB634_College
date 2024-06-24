package com.mvi.CSCB634College.program;

import com.mvi.CSCB634College.major.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findAllByYear(Integer year);

    @Query("SELECT p FROM Program p WHERE p.course.major = :major AND p.year = :year")
    List<Program> findAllByYearAndMajor(
            @Param("year") Integer year,
            @Param("major") Major major);

}
