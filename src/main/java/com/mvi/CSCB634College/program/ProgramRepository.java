package com.mvi.CSCB634College.program;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findAllByYear(Integer year);
}
