package com.mvi.CSCB634College.college;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
    List<College> findByNameIgnoreCase(String name);
    List<College> findByRector_EmailAllIgnoreCase(String email);

    List<College> findByNameIgnoreCaseOrAddressIgnoreCase(String name, String address);
}
