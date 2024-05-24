package com.mvi.CSCB634College.course;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/admin/create/")
    public ResponseEntity<DtoCourse> createCourse(
            @Valid @RequestBody DtoCourse dtoCourse) {
        return ResponseEntity.ok(courseService.createCourse(dtoCourse));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoCourse> getCourseById(
            @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/getAllByMajor/{majorId}")
    public ResponseEntity<List<DtoCourse>> getAllByMajor(
            @PathVariable Long majorId) {
        return ResponseEntity.ok(courseService.getAllByMajor(majorId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoCourse> updateCourse(
            @PathVariable("id") Long id,
            @RequestBody DtoCourse dtoCourse) {
        return ResponseEntity.ok(courseService.updateCourse(id, dtoCourse));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    
}
