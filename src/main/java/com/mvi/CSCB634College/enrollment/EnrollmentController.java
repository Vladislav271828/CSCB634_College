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

    @GetMapping("/getAllByStudentAndYear/{studentId}/{year}")
    public ResponseEntity<List<DtoEnrollmentResponse>> getEnrollmentsByStudentAndYear(
            @PathVariable Integer studentId,
            @PathVariable Integer year) {
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentByStudentIdAndYear(studentId, year));
    }

    @GetMapping("/getAllByProfessorIdAndYearAndCourse/{professorId}/{year}/{courseId}")
    public ResponseEntity<List<DtoEnrollmentResponse>> getAllByProfessorIdAndYearAndCourse(
            @PathVariable Integer professorId,
            @PathVariable Integer year,
            @PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getAllEnrollmentByProfessorIdAndYearAndCourse(professorId, year, courseId));
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

    @GetMapping("/getGradesBy/{something}/{somethingId}")
    public ResponseEntity<List<Integer>> getGrades(
            @PathVariable String something,
            @PathVariable Long somethingId
    ){
        return ResponseEntity.ok(enrollmentService.getGrades(something, somethingId));
    }

    @GetMapping("/professor/finalGrade/{enrollmentId}/{grade}")
    public ResponseEntity<DtoEnrollmentResponse> getGrades(
            @PathVariable Long enrollmentId,
            @PathVariable Integer grade
    ){
        return ResponseEntity.ok(enrollmentService.changeGrade(enrollmentId, grade));
    }
}
