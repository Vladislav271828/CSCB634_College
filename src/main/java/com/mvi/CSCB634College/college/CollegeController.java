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
            @PathVariable Long id) {
        return ResponseEntity.ok(collegeService.getCollegeById(id));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<DtoCollegeRequest> updateCollege(
            @PathVariable("id") Long id,
            @RequestBody DtoCollegeRequest dtoCollegeRequest) {
        return ResponseEntity.ok(collegeService.updateCollege(id, dtoCollegeRequest));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteCollege(
            @PathVariable Long id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/getAll")
    public ResponseEntity<List<DtoCollegeRequest>> getAllColleges() {
        return ResponseEntity.ok(collegeService.getAllColleges());
    }

}
