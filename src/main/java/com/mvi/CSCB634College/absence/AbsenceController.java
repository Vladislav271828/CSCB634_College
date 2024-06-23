package com.mvi.CSCB634College.absence;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/absence")
@RequiredArgsConstructor
public class AbsenceController {

    private final AbsenceService absenceService;

    @PostMapping("/professor/create")
    public ResponseEntity<DtoAbsence> createAbsence(
            @Valid @RequestBody DtoAbsence dtoAbsence) {
        return ResponseEntity.ok(absenceService.createAbsence(dtoAbsence));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoAbsence> getAbsenceById(
            @PathVariable Long id) {
        return ResponseEntity.ok(absenceService.getAbsenceById(id));
    }

    @GetMapping("/getAllByEnrollment/{enrollmentId}")
    public ResponseEntity<List<DtoAbsence>> getAllByEnrollment(
            @PathVariable Long enrollmentId) {
        return ResponseEntity.ok(absenceService.getAllByEnrollment(enrollmentId));
    }

    @PutMapping("/professor/{id}/update")
    public ResponseEntity<DtoAbsence> updateAbsence(
            @PathVariable("id") Long id,
            @RequestBody DtoAbsence dtoAbsence) {
        return ResponseEntity.ok(absenceService.updateAbsence(id, dtoAbsence));
    }

    @DeleteMapping("/professor/{id}/delete")
    public ResponseEntity<Void> deleteAbsence(
            @PathVariable Long id) {
        absenceService.deleteAbsence(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getNumberOfAbsencesBy/{something}/{somethingId}")
    public ResponseEntity<List<Long>> getGrades(
            @PathVariable String something,
            @PathVariable Long somethingId
    ){
        return ResponseEntity.ok(absenceService.getNumber(something, somethingId));
    }
}