package com.mvi.CSCB634College.ProfessorQualification;


import com.mvi.CSCB634College.security.auth.AuthenticationResponse;
import com.mvi.CSCB634College.security.auth.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/professorQualification")
@RequiredArgsConstructor
public class ProfessorQualificationController {
    private final ProfessorQualificationService professorQualificationService;


    @PostMapping("/create")
    public ResponseEntity<ProfessorQualificationResponse> createProfessorQualification(@Valid @RequestBody ProfessorQualificationRequest request){
        return ResponseEntity.ok(professorQualificationService.createProfessorQualification(request));
    }

    @PutMapping("/update")
    public ResponseEntity<ProfessorQualificationResponse> updateProfessorQualification(@Valid @RequestBody ProfessorQualificationRequest request){
        return ResponseEntity.ok(professorQualificationService.updateProfessorQualification(request));
    }


    @DeleteMapping("/delete/{professorQualificationId}")
    public ResponseEntity<Void> deleteProfessorQualification(@PathVariable Integer professorQualificationId){
//        professorQualificationService.deleteProfessorQualification(professorQualificationId);
        return ResponseEntity.noContent().build();
    }

}
