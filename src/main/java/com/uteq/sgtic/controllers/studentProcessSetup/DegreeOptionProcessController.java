package com.uteq.sgtic.controllers.studentProcessSetup;

import com.uteq.sgtic.dtos.DegreeOptionDTO;
import com.uteq.sgtic.services.IDegreeOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degree-options")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DegreeOptionProcessController {

    private final IDegreeOptionService degreeOptionService;

//    @GetMapping("/active")
//    public ResponseEntity<List<DegreeOptionDTO>> getActiveOptions() {
//        return ResponseEntity.ok(degreeOptionService.getActiveOptions());
//    }

    @GetMapping
    public ResponseEntity<List<DegreeOptionDTO>> getAllOptions() {
        return ResponseEntity.ok(degreeOptionService.getAllOptions());
    }

    @PostMapping
    public ResponseEntity<DegreeOptionDTO> saveOption(@RequestBody DegreeOptionDTO dto) {
        return new ResponseEntity<>(degreeOptionService.save(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> toggleStatus(@PathVariable Integer id, @RequestBody Boolean status) {
        degreeOptionService.toggleStatus(id, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Integer id) {
        degreeOptionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}