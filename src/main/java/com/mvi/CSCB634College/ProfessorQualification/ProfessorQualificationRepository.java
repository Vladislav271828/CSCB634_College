package com.mvi.CSCB634College.ProfessorQualification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfessorQualificationRepository  extends JpaRepository<ProfessorQualification, Integer> {
    @Query("select p from ProfessorQualification p where p.professor.id = ?1")
    List<ProfessorQualification> findByProfessor_Id(Integer id);
    @Query("select p from ProfessorQualification p where p.course.id = ?1 and p.professor.id = ?2")
    Optional<ProfessorQualification> findByCourse_IdAndProfessor_Id(Long id, Integer id1);
    @Query("select (count(p) > 0) from ProfessorQualification p where p.course.id = ?1 and p.professor.id = ?2")
    boolean existsByCourse_IdAndProfessor_Id(Long id, Integer id1);
    @Query("select (count(p) > 0) from ProfessorQualification p where p.professor.id = ?1")
    boolean existsByProfessor_Id(Integer id);

}
