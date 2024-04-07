package com.mvi.CSCB634College.college;

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
    public ResponseEntity<DtoCollege> createCollege(DtoCollege dtoCollege) {
        return ResponseEntity.ok(collegeService.createCollege(dtoCollege));
    }

    @GetMapping("/getById")
    public ResponseEntity<DtoCollege> getCollegeById(Integer id) {
        return ResponseEntity.ok(collegeService.getCollegeById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<DtoCollege> updateCollege(Integer id, DtoCollege dtoCollege) {
        return ResponseEntity.ok(collegeService.updateCollege(id, dtoCollege));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCollege(Integer id) {
        collegeService.deleteCollege(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DtoCollege>> getAllColleges() {
        return ResponseEntity.ok(collegeService.getAllColleges());
    }

    @PatchMapping("/addRector")
    public ResponseEntity<DtoCollege> addRector(Integer collegeId, Integer rectorId) {
        return ResponseEntity.ok(collegeService.addRectorToCollege(collegeId, rectorId));
    }

    @PatchMapping("/removeRector")
    public ResponseEntity<DtoCollege> removeRector(Integer collegeId) {
        return ResponseEntity.ok(collegeService.removeRectorFromCollege(collegeId));
    }


    

}
