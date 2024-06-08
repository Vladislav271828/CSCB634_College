package com.mvi.CSCB634College.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Query("select s from Student s where s.user.id = :id")
    Optional<Student> findStudentByUserId(@Param("id") Integer id);
}
