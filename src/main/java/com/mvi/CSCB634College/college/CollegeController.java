package com.mvi.CSCB634College.college;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/college")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService collegeService;

    @PostMapping("/create")
    public ResponseEntity<DtoCollege> createCollege(
            @Valid @RequestBody DtoCollege dtoCollege) {
        return ResponseEntity.ok(collegeService.createCollege(dtoCollege));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoCollege> getCollegeById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(collegeService.getCollegeById(id));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DtoCollege> updateCollege(
            @PathVariable ("id") Integer id,
            @Valid @RequestBody DtoCollege dtoCollege) {
        return ResponseEntity.ok(collegeService.updateCollege(id, dtoCollege));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCollege(
            @PathVariable Integer id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DtoCollege>> getAllColleges() {
        return ResponseEntity.ok(collegeService.getAllColleges());
    }

    @PatchMapping("/{id}/addRector/{rectorId}")
    public ResponseEntity<DtoCollege> addRector(
            @PathVariable Integer id,
            @PathVariable Integer rectorId) {
        return ResponseEntity.ok(collegeService.addRectorToCollege(id, rectorId));
    }

    @PatchMapping("/{id}/removeRector")
    public ResponseEntity<DtoCollege> removeRector(
            @PathVariable Integer id) {
        return ResponseEntity.ok(collegeService.removeRectorFromCollege(id));
    }


}
