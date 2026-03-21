package com.uteq.sgtic.controllers.requestAccess;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uteq.sgtic.dtos.requestAccess.RequestAccessDTO;
import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import com.uteq.sgtic.services.requestAccess.IRequestAccessServices;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/request-access")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestAccessController {

    private final IRequestAccessServices requestAccessService;

    // 1. Endpoint para enviar la solicitud (NUEVO)
    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> createRequest(@RequestBody RequestAccessDTO requestDto) {
        requestAccessService.createRequest(requestDto);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Su solicitud ha sido enviada exitosamente. Se encuentra pendiente de revisión.");
        
        return ResponseEntity.ok(response);
    }

    // 2. Endpoint para obtener todas las Facultades
    @GetMapping("/faculties")
    public ResponseEntity<List<SelectionItemDTO>> getFaculties() {
        return ResponseEntity.ok(requestAccessService.getFaculties());
    }

    // 3. Endpoint para obtener las Carreras dependientes de la Facultad
    @GetMapping("/careers/by-faculty/{facultyId}")
    public ResponseEntity<List<SelectionItemDTO>> getCareersByFaculty(@PathVariable Integer facultyId) {
        return ResponseEntity.ok(requestAccessService.getCareersByFaculty(facultyId));
    }
}