package com.uteq.sgtic.controllers.InstitutionalStructurePackControllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.uteq.sgtic.dtos.institutionalstructure.ManageCareerDTO;
import com.uteq.sgtic.services.InstitutionalStructurePackServices.IManageCareerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Map;

@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.OPTIONS})

public class ManageCareerControllers {
    private final IManageCareerServices iManageCareerServices;

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCareer(@PathVariable("id") Integer id, @RequestBody ManageCareerDTO dto) {
        try {
            dto.setIdCareer(id);
            iManageCareerServices.updateCareer(dto);
            return ResponseEntity.ok(Map.of("message", "Carrera actualizada exitosamente"));
        } catch(RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno en el servidor"));
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> toggleCareerStatus(@PathVariable("id") Integer id) {
        try {
            iManageCareerServices.toggleCareerStatus(id);
            return ResponseEntity.ok(Map.of("message", "Estado de la carrera actualizado"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno en el servidor"));
        }
    }
}
