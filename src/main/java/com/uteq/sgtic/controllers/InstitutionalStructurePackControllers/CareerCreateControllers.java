package com.uteq.sgtic.controllers.InstitutionalStructurePackControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uteq.sgtic.dtos.institutionalstructure.CareerCreateDTO;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.ICareerCreateServices;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/create-careers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")

public class CareerCreateControllers {
    
    private final ICareerCreateServices iCareerCreateServices;

    @PostMapping
    public ResponseEntity<?> createCareer(@RequestBody CareerCreateDTO dto){
        try{
            iCareerCreateServices.createCareer(dto);
            return ResponseEntity.ok(Map.of("message", "Carrera creada exitosamente"));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error en el servidor"));
        }
    }
}
