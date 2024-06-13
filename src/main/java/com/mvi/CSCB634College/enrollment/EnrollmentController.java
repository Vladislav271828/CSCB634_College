package com.mvi.CSCB634College.enrollment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @PostMapping("/admin/create")
    public ResponseEntity<DtoEnrollment> createEnrollment(
            @Valid @RequestBody DtoEnrollment dtoEnrollment) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(dtoEnrollment));
    }

    @GetMapping("/getById/{enrollmentId}")
    public ResponseEntity<DtoEnrollment> getEnrollmentById(
            @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(enrollmentId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoEnrollment> updateEnrollment(
            @PathVariable Long id,
            @Valid @RequestBody DtoEnrollment dtoEnrollment) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, dtoEnrollment));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteEnrollment(
            @PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }




}