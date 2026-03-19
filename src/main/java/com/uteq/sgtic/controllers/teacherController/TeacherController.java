package com.uteq.sgtic.controllers.teacherController;

import com.uteq.sgtic.dtos.teacherDTO.TeacherDTO;
import com.uteq.sgtic.dtos.teacherDTO.TeacherSaveDTO;
import com.uteq.sgtic.services.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "*")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/facultad/{idUsuarioLogueado}")
    public ResponseEntity<List<TeacherDTO>> listarDocentes(@PathVariable Integer idUsuarioLogueado) {
        List<TeacherDTO> docentes = teacherService.getDocentesPorCoordinador(idUsuarioLogueado);
        return ResponseEntity.ok(docentes);
    }


    @PostMapping("/save")
    public ResponseEntity<?> guardarDocente(@RequestBody TeacherSaveDTO docenteData) {
        try {
            teacherService.guardarDocente(docenteData);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("Error al guardar docente: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Error al guardar el docente: " + e.getMessage());
        }
    }

    @PutMapping("/{idDocente}/estado")
    public ResponseEntity<?> elimnarDocente(@PathVariable Integer idDocente, @RequestBody Map<String, String> body) {
        try {
            String nuevoEstado = body.get("estado");
            teacherService.callActualizarEstado(idDocente, nuevoEstado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
}