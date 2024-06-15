package com.mvi.CSCB634College.department;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService DepartmentService;

    @PostMapping("/admin/create")
    public ResponseEntity<DtoDepartment> createDepartment(
            @Valid @RequestBody DtoDepartment dtoDepartment) {
        return ResponseEntity.ok(DepartmentService.createDepartment(dtoDepartment));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoDepartment> getDepartmentById(
            @PathVariable Long id) {
        return ResponseEntity.ok(DepartmentService.getDepartmentById(id));
    }

    @GetMapping("/getAllByCollege/{collegeId}")
    public ResponseEntity<List<DtoDepartment>> getAllByCollege(
            @PathVariable Long collegeId) {
        return ResponseEntity.ok(DepartmentService.getAllByCollege(collegeId));
    }

    @GetMapping("/getAllByFaculty/{facultyId}")
    public ResponseEntity<List<DtoDepartment>> getAllByFaculty(
            @PathVariable Long facultyId) {
        return ResponseEntity.ok(DepartmentService.getAllByFaculty(facultyId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoDepartment> updateDepartment(
            @PathVariable("id") Long id,
            @RequestBody DtoDepartment dtoDepartment) {
        return ResponseEntity.ok(DepartmentService.updateDepartment(id, dtoDepartment));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Long id) {
        DepartmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
