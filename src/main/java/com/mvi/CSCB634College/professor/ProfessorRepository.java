package com.mvi.CSCB634College.professor;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Transactional
public interface ProfessorRepository extends JpaRepository<Professor, Integer> {
    @Query("select p from Professor p where p.department.id = ?1")
    List<Professor> findByDepartment_Id(Long id);
    @Query("select p from Professor p where p.department.college.id = ?1")
    List<Professor> findByDepartment_College_Id(Long id);
    @Query("select p from Professor p where p.user.id = :id")
    Optional<Professor> findProfessorByUserId(@Param("id") Integer id);
}
