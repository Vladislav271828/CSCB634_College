package com.mvi.CSCB634College.program;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/program")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramService programService;


    @PostMapping("/admin/create")
    public ResponseEntity<DtoProgramResponse> createProgram(
            @Valid @RequestBody DtoProgram dtoProgram) {
        return ResponseEntity.ok(programService.createProgram(dtoProgram));
    }

    @GetMapping("/getByYear/{year}")
    public ResponseEntity<List<DtoProgramResponse>> getProgramsByYear(
            @PathVariable Integer year) {
        return ResponseEntity.ok(programService.getProgramsByYear(year));
    }

    @GetMapping("/getById/{programId}")
    public ResponseEntity<DtoProgramResponse> getProgramById(
            @PathVariable Long programId) {
        return ResponseEntity.ok(programService.getProgramById(programId));
    }

    @PutMapping("/admin/{id}/update")
    public ResponseEntity<DtoProgramResponse> updateProgram(
            @PathVariable Long id,
            @Valid @RequestBody DtoProgram dtoProgram) {
        return ResponseEntity.ok(programService.editProgram(id, dtoProgram));
    }

    @DeleteMapping("/admin/{id}/delete")
    public ResponseEntity<Void> deleteProgram(
            @PathVariable Long id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }


}