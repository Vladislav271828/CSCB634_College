package com.mvi.CSCB634College.professor;

import com.mvi.CSCB634College.user.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    @GetMapping("/admin/getAllProfessorsByCollege/{collegeId}")
    public ResponseEntity<List<ResponseUser>> getAllByCollege(
            @PathVariable Long collegeId) {
        return ResponseEntity.ok(professorService.getAllProfessorsByCollegeId(collegeId));
    }

    @GetMapping("/admin/getAllProfessorsByDepartment/{departmentId}")
    public ResponseEntity<List<ResponseUser>> getAllByDepartment(
            @PathVariable Long departmentId) {
        return ResponseEntity.ok(professorService.getAllProfessorsByDepartmentId(departmentId));
    }
}
