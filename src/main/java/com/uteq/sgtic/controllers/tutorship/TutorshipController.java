package com.uteq.sgtic.controllers.tutorship;

import com.uteq.sgtic.dtos.tutorship.AssignedWorkDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipReportDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipRequestDTO;
import com.uteq.sgtic.dtos.tutorship.TutorshipResponseDTO;
import com.uteq.sgtic.services.tutorship.TutorshipService;
import com.uteq.sgtic.entities.WorkTutoring;
import com.uteq.sgtic.repository.tutorship.WorkTutoringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher/tutorships")
@RequiredArgsConstructor
public class TutorshipController {
    private final TutorshipService tutorshipService;
    private final WorkTutoringRepository workTutoringRepository;

    @PostMapping
    public ResponseEntity<?> scheduleTutorship (@RequestBody TutorshipRequestDTO request, Authentication authentication) {
        try{
            tutorshipService.scheduleTutorship(request, authentication.getName());
            return ResponseEntity.ok(Map.of("message", "Tutorship scheduled successfully"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/report")
    public ResponseEntity<?> uploadReport(
            @PathVariable("id") Integer idTutoring,
            @RequestPart("reportData") TutorshipReportDTO reportData,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) {

        try {
            String email = authentication.getName();
            tutorshipService.registerTutorshipReport(idTutoring, reportData, email, file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Informe registrado y guardado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/{id}/report/download")
    public ResponseEntity<Resource> downloadReport(@PathVariable Integer id) {
        try {
            WorkTutoring tutorship = workTutoringRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tutoría no encontrada"));

            if (tutorship.getReportUrl() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get("uploads/reports/").resolve(tutorship.getReportUrl()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
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
