package com.uteq.sgtic.controllers.student;

import com.uteq.sgtic.dtos.AcademicPeriodDTO;
import com.uteq.sgtic.repository.UserRepository;
import com.uteq.sgtic.repository.student.StudentAcademicPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student/periodos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentAcademicPeriodController {

    private final StudentAcademicPeriodRepository studentAcademicPeriodRepository;
    private final UserRepository userRepository;

    @GetMapping("/aceptados")
    public ResponseEntity<List<AcademicPeriodDTO>> getAcceptedPeriods(Principal principal) {
        String email = principal.getName();

        Integer userId = userRepository.findCredentialsByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"))
                .getUserId();

        List<AcademicPeriodDTO> dtos = studentAcademicPeriodRepository.findAcceptedPeriodsByUserId(userId)
                .stream()
                .map(p -> new AcademicPeriodDTO(
                        p.getIdPeriod(),
                        p.getName(),
                        p.getStartDate(),
                        p.getEndDate(),
                        Boolean.TRUE.equals(p.getActive()),
                        p.getEnrollmentDeadline(),
                        p.getPlazoCambioTema(),    //  NUEVO
                        p.getMinimoAvances()        //  NUEVO
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}