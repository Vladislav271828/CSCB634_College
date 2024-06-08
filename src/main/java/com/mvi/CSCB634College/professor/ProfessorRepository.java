package com.mvi.CSCB634College.professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    @Query("select p from Professor p where p.user.id = :id")
    Optional<Professor> findProfessorByUserId(@Param("id") Integer id);
}
