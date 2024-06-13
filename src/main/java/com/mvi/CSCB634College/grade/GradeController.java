package com.mvi.CSCB634College.grade;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grade")
@RequiredArgsConstructor
public class GradeController {

  private final GradeService gradeService;

  @PostMapping("/professor/create")
  public ResponseEntity<DtoGrade> createGrade(
          @Valid @RequestBody DtoGrade dtoGrade) {
      return ResponseEntity.ok(gradeService.createGrade(dtoGrade));
  }

  @GetMapping("/getById/{id}")
  public ResponseEntity<DtoGrade> getGradeById(
          @PathVariable Long id) {
      return ResponseEntity.ok(gradeService.getGradeById(id));
  }

  @GetMapping("/getAllByEnrollment/{enrollmentId}")
  public ResponseEntity<List<DtoGrade>> getAllByEnrollment(
          @PathVariable Long enrollmentId) {
      return ResponseEntity.ok(gradeService.getAllByEnrollment(enrollmentId));
  }

  @PutMapping("/professor/{id}/update")
  public ResponseEntity<DtoGrade> updateGrade(
          @PathVariable("id") Long id,
          @RequestBody DtoGrade dtoGrade) {
      return ResponseEntity.ok(gradeService.updateGrade(id, dtoGrade));
  }
  @DeleteMapping("/professor/{id}/delete")
  public ResponseEntity<Void> deleteGrade(
          @PathVariable Long id) {
      gradeService.deleteGrade(id);
      return ResponseEntity.noContent().build();
  }
}
