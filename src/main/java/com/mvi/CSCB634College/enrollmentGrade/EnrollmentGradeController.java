package com.mvi.CSCB634College.enrollmentGrade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/enrollmentGrade")
@RequiredArgsConstructor
public class EnrollmentGradeController {

    private final EnrollmentGradeService enrollmentGradeService;

    @PostMapping("/professor/create")
    public ResponseEntity<DtoEnrollmentGradeResponse> createEnrollmentGrade(
            @Valid @RequestBody DtoEnrollmentGrade dtoEnrollmentGrade) {
        return ResponseEntity.ok(enrollmentGradeService.createEnrollmentGrade(dtoEnrollmentGrade));
    }

    @PutMapping("/professor/{id}/update")
    public ResponseEntity<DtoEnrollmentGradeResponse> updateEnrollmentGrade(
            @PathVariable Long id,
            @Valid @RequestBody DtoEnrollmentGrade dtoEnrollmentGrade) {
        return ResponseEntity.ok(enrollmentGradeService.updateEnrollmentGrade(id, dtoEnrollmentGrade));
    }

    @DeleteMapping("/professor/{id}/delete")
    public ResponseEntity<Void> deleteEnrollmentGrade(
            @PathVariable Long id) {
        enrollmentGradeService.deleteEnrollmentGrade(id);
        return ResponseEntity.noContent().build();
    }



}
