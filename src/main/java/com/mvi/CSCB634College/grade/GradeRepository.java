package com.mvi.CSCB634College.grade;

import com.mvi.CSCB634College.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    List<Grade> findAllByEnrollment(Enrollment enrollment);
}
