package com.mvi.CSCB634College.major;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/major")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("/admin/create")
    public ResponseEntity<DtoMajor> createMajor(
            @Valid @RequestBody DtoMajor dtoMajor) {
        return ResponseEntity.ok(majorService.createMajor(dtoMajor));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoMajor> getMajorById(
            @PathVariable Long id) {
        return ResponseEntity.ok(majorService.getMajorById(id));
    }

    @GetMapping("/getAllByDepartment/{departmentId}")
    public ResponseEntity<List<DtoMajor>> getAllByDepartment(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(majorService.getAllByDepartment(departmentId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoMajor> updateMajor(
            @PathVariable("id") Long id,
            @RequestBody DtoMajor dtoMajor) {
        return ResponseEntity.ok(majorService.updateMajor(id, dtoMajor));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteMajor(
            @PathVariable Long id) {
        majorService.deleteMajor(id);
        return ResponseEntity.noContent().build();
    }

    
}
