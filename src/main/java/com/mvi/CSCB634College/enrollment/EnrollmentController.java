package com.mvi.CSCB634College.enrollment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollment")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;


    @PostMapping("/admin/create")
    public ResponseEntity<DtoEnrollmentResponse> createEnrollment(
            @Valid @RequestBody DtoEnrollment dtoEnrollment) {
        return ResponseEntity.ok(enrollmentService.createEnrollment(dtoEnrollment));
    }

    @GetMapping("/getById/{enrollmentId}")
    public ResponseEntity<DtoEnrollmentResponse> getEnrollmentById(
            @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(enrollmentId));
    }

    @GetMapping("/getByStudentAndYear/{studentId}/{year}")
    public ResponseEntity<List<DtoEnrollmentResponse>> getEnrollmentsByStudentAndYear(
            @PathVariable Integer studentId,
            @PathVariable Integer year) {
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentByStudentIdAndYear(studentId, year));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoEnrollmentResponse> updateEnrollment(
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
