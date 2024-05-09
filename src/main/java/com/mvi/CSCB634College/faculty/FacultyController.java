package com.mvi.CSCB634College.faculty;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping("/admin/create/{collegeId}")
    public ResponseEntity<DtoFaculty> createFaculty(
            @Valid @RequestBody DtoFaculty dtoFaculty
            , @PathVariable Integer collegeId) {
        return ResponseEntity.ok(facultyService.createFaculty(dtoFaculty, collegeId));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoFaculty> getFacultyById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoFaculty> updateFaculty(
            @PathVariable("id") Integer id,
            @RequestBody DtoFaculty dtoFaculty) {
        return ResponseEntity.ok(facultyService.updateFaculty(id, dtoFaculty));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteFaculty(
            @PathVariable Integer id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }  
}
