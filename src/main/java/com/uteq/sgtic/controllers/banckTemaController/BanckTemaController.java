package com.uteq.sgtic.controllers.banckTemaController;

import com.uteq.sgtic.dtos.banckTemaDTO.BanckTemaDTO;
import com.uteq.sgtic.services.IBanckTemaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banco-temas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BanckTemaController {
    private final IBanckTemaService banckTemaService;

    @GetMapping("/carrera/{idCarrera}")
    public ResponseEntity<List<BanckTemaDTO>> listarTemas(@PathVariable Integer idCarrera) {
        return ResponseEntity.ok(banckTemaService.listarTemasPorCarrera(idCarrera));
    }

    @PostMapping("/guardar/{idCarrera}")
    public ResponseEntity<Void> guardarTema(@RequestBody BanckTemaDTO dto, @PathVariable Integer idCarrera) {
        banckTemaService.crearTemaConComision(dto, idCarrera);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarTema(@RequestBody BanckTemaDTO temaDTO) {
        banckTemaService.actualizarTema(temaDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarTema(@PathVariable Integer id) {
        banckTemaService.eliminar(id);
        return ResponseEntity.ok().build();
    }
}


