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

    @PostMapping("/admin/create/{collegeId}")
    public ResponseEntity<DtoDepartment> createDepartment(
            @Valid @RequestBody DtoDepartment dtoDepartment
            , @PathVariable Integer collegeId) {
        return ResponseEntity.ok(DepartmentService.createDepartment(dtoDepartment, collegeId));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoDepartment> getDepartmentById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(DepartmentService.getDepartmentById(id));
    }

    @GetMapping("/getAllByCollege/{collegeId}")
    public ResponseEntity<List<DtoDepartment>> getAllByCollege(
            @PathVariable Integer collegeId) {
        return ResponseEntity.ok(DepartmentService.getAllByCollege(collegeId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoDepartment> updateDepartment(
            @PathVariable("id") Integer id,
            @RequestBody DtoDepartment dtoDepartment) {
        return ResponseEntity.ok(DepartmentService.updateDepartment(id, dtoDepartment));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteDepartment(
            @PathVariable Integer id) {
        DepartmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}
