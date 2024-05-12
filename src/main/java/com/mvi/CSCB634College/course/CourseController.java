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

    @PostMapping("/admin/create/{majorId}")
    public ResponseEntity<DtoCourse> createCourse(
            @Valid @RequestBody DtoCourse dtoCourse
            , @PathVariable Integer majorId) {
        return ResponseEntity.ok(courseService.createCourse(dtoCourse, majorId));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DtoCourse> getCourseById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/getAllByMajor/{majorId}")
    public ResponseEntity<List<DtoCourse>> getAllByMajor(
            @PathVariable Integer majorId) {
        return ResponseEntity.ok(courseService.getAllByMajor(majorId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoCourse> updateCourse(
            @PathVariable("id") Integer id,
            @RequestBody DtoCourse dtoCourse) {
        return ResponseEntity.ok(courseService.updateCourse(id, dtoCourse));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    
}
