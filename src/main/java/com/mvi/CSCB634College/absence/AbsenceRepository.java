package com.mvi.CSCB634College.absence;


import com.mvi.CSCB634College.enrollment.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

    List<Absence> findAllByEnrollment(Enrollment enrollment);
}
