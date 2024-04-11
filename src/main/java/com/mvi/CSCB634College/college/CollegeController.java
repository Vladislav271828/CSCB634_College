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

    @PostMapping("/admin/create")
    public ResponseEntity<DtoCollegeRequest> createCollege(
            @Valid @RequestBody DtoCollegeRequest dtoCollegeRequest) {
        return ResponseEntity.ok(collegeService.createCollege(dtoCollegeRequest));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoCollegeRequest> getCollegeById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(collegeService.getCollegeById(id));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<DtoCollegeRequest> updateCollege(
            @PathVariable("id") Integer id,
            @RequestBody DtoCollegeRequest dtoCollegeRequest) {
        return ResponseEntity.ok(collegeService.updateCollege(id, dtoCollegeRequest));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteCollege(
            @PathVariable Integer id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<List<DtoCollegeRequest>> getAllColleges() {
        return ResponseEntity.ok(collegeService.getAllColleges());
    }

    @PatchMapping("/admin/{id}/addRector/{rectorId}")
    public ResponseEntity<DtoCollegeRequest> addRector(
            @PathVariable Integer id,
            @PathVariable Integer rectorId) {
        return ResponseEntity.ok(collegeService.addRectorToCollege(id, rectorId));
    }

    @PatchMapping("/admin/{id}/removeRector")
    public ResponseEntity<DtoCollegeRequest> removeRector(
            @PathVariable Integer id) {
        return ResponseEntity.ok(collegeService.removeRectorFromCollege(id));
    }


}
