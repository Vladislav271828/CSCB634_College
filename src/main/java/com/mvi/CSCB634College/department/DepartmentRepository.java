package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.faculty.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select d from Department d inner join d.professors professors where professors.id = ?1")
    Optional<Department> findByProfessors_Id(Integer id);
    @Query("select (count(d) > 0) from Department d where d.id = ?1")
    boolean doesDepartmentExist(Long id);
    @Query("""
            select d from Department d left join d.professors professors
            where professors.id = ?1 or d.departmentHead.id = ?2""")
    Optional<Department> findDepartmentByProfessorId(Integer id, Integer id1);
    @Query("select d from Department d where d.departmentHead.id = ?1")
    Optional<Department> findByDepartmentHead_Id(Integer id);
    @Query("select (count(d) > 0) from Department d where d.departmentHead.id = ?1")
    boolean isUserAlreadyDepartmentHead(Integer id);
    @Query("select (count(d) > 0) from Department d where d.id = :id and d.departmentHead is null")
    boolean isDepartmentHeadNull(@Param("id") Long id);

    @Query("select d from Department d where d.faculty.college.id = ?1")
    List<Department> findAllByCollege(Long collegeId);

    @Override
    Optional<Department> findById(Long aLong);

    List<Department> findAllByFaculty(Faculty faculty);
}
