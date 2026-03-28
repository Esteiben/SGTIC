package com.uteq.sgtic.controllers.InstitutionalStructurePackControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;
import com.uteq.sgtic.dtos.institutionalstructure.FacultyCreateDTO;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.IFacultyCreateServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/create-faculty")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class FacultyCreateControllers {
    
    private final IFacultyCreateServices iFacultyCreateServices;

    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyCreateDTO dto){
        try{
            iFacultyCreateServices.createFaculty(dto);
            return ResponseEntity.ok(Map.of("message", "Facultad creada exitosamente"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error en el servidor"));
        }
    }
    
}
