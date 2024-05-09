package com.mvi.CSCB634College.faculty;

import com.mvi.CSCB634College.college.College;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {


    List<Faculty> findAllByCollege(College college);
}
