package com.uteq.sgtic.controllers.tutorship;

import com.uteq.sgtic.dtos.tutorship.AssignedWorkDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipRequestDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipResponseDTO;
import com.uteq.sgtic.services.tutorship.TutorshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher/tutorships")
@RequiredArgsConstructor
public class TutorshipController {
    private final TutorshipService tutorshipService;

    @PostMapping
    public ResponseEntity<?> scheduleTutorship (@RequestBody TutorshipRequestDTO request, Authentication authentication) {
        try{
            tutorshipService.scheduleTutorship(request, authentication.getName());
            return ResponseEntity.ok(Map.of("message", "Tutorship scheduled successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<TutorshipResponseDTO>> getMyTutorships(Authentication authentication) {
        return ResponseEntity.ok(tutorshipService.getMyTutorships(authentication.getName()));
    }

    @GetMapping("/assigned-works")
    public ResponseEntity<List<AssignedWorkDTO>> getAssignedWorks(Authentication authentication) {
        return ResponseEntity.ok(tutorshipService.getAssignedWorksForDirector(authentication.getName()));
    }
}
