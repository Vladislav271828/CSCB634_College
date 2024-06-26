package com.mvi.CSCB634College.faculty;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping("/admin/create")
    public ResponseEntity<DtoFaculty> createFaculty(
            @Valid @RequestBody DtoFaculty dtoFaculty) {
        return ResponseEntity.ok(facultyService.createFaculty(dtoFaculty));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoFaculty> getFacultyById(
            @PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @GetMapping("/getAllByCollege/{collegeId}")
    public ResponseEntity<List<DtoFaculty>> getAllByCollege(
            @PathVariable Long collegeId) {
        return ResponseEntity.ok(facultyService.getAllByCollege(collegeId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoFaculty> updateFaculty(
            @PathVariable("id") Long id,
            @RequestBody DtoFaculty dtoFaculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(id, dtoFaculty));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteFaculty(
            @PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }  
}
