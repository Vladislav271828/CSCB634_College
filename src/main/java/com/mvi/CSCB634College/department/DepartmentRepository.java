package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d from Department d where d.departmentHead.id = ?1")
    Optional<Department> findByDepartmentHead_Id(Integer id);
    @Query("select (count(d) > 0) from Department d where d.departmentHead.id = ?1")
    boolean isUserAlreadyDepartmentHead(Integer id);
    @Query("select (count(d) > 0) from Department d where d.id = :id and d.departmentHead is null")
    boolean isDepartmentHeadNull(@Param("id") Long id);

    List<Department> findAllByCollege(College college);
}
