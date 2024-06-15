package com.mvi.CSCB634College.student;

import com.mvi.CSCB634College.professor.ProfessorService;
import com.mvi.CSCB634College.user.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    @GetMapping("/admin/getAllStudentsByMajorId/{majorId}")
    public ResponseEntity<List<ResponseUser>> getAllStudentsByMajorId(
            @PathVariable Long majorId) {
        return ResponseEntity.ok(studentService.getAllStudentsByMajorId(majorId));
    }

}
