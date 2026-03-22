package com.uteq.sgtic.controllers.topicProposedControllers;

import com.uteq.sgtic.services.ITopicProposedService;
import com.uteq.sgtic.dtos.pendingProposalDTO.PendingProposalDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/propuestas-estudiante")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TopicProposedControllers {

    private final ITopicProposedService proposedService;

    @GetMapping("/pendientes/{idCoordinador}")
    public ResponseEntity<List<PendingProposalDTO>> listarPendientes(@PathVariable Integer idCoordinador) {
        return ResponseEntity.ok(proposedService.obtenerPropuestasPendientes(idCoordinador));
    }

    @PostMapping("/responder")
    public ResponseEntity<Void> responderPropuesta(
            @RequestParam Integer idPropuesta,
            @RequestParam String estado,
            @RequestParam(required = false) String motivo) {
        proposedService.procesarPropuesta(idPropuesta, estado, motivo);
        return ResponseEntity.ok().build();
    }
}
