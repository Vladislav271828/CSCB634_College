package com.mvi.CSCB634College.major;

import com.mvi.CSCB634College.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<Major, Long> {
    @Query("select (count(m) > 0) from Major m where m.id = ?1")
    boolean doesMajorExist(Long id);
    @Query("select m from Major m where m.id = ?1")
    Optional<Major> findMajorById(Long id);

    List<Major> findAllByDepartment(Department department);
}
