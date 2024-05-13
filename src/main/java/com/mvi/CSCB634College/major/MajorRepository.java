package com.mvi.CSCB634College.major;

import com.mvi.CSCB634College.department.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Integer> {

    List<Major> findAllByDepartment(Department department);
}
