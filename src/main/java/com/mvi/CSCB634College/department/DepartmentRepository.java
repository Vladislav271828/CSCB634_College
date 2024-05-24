package com.mvi.CSCB634College.department;

import com.mvi.CSCB634College.college.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByCollege(College college);
}
