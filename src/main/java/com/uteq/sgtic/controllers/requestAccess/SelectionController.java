package com.uteq.sgtic.controllers.requestAccess;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uteq.sgtic.services.requestAccess.ISelectionServices;
import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;

import java.util.List;

@RestController
@RequestMapping("/api/public/selection")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SelectionController {
    private final ISelectionServices selectionServices;

    @GetMapping("/faculties")
    public ResponseEntity<List<SelectionItemDTO>> getFaculties() {
        return ResponseEntity.ok(selectionServices.getActiveFaculties());
    }

    @GetMapping("/faculties/{facultyId}/careers")
    public ResponseEntity<List<SelectionItemDTO>> getCareersByFaculty(@PathVariable Integer facultyId) {
        return ResponseEntity.ok(selectionServices.getActiveCareersByFaculty(facultyId));
    }
}
