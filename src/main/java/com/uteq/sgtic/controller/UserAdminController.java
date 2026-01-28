package com.uteq.sgtic.controller;

import com.uteq.sgtic.dtos.UserCreateDTO;
import com.uteq.sgtic.dtos.UserStatusUpdateDTO;
import com.uteq.sgtic.service.IUserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserAdminController {
    private final IUserAdminService userAdminService;

    @PostMapping
    public ResponseEntity<Void> createUser(
            @RequestBody UserCreateDTO dto) {

        userAdminService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @RequestBody UserStatusUpdateDTO dto) {

        userAdminService.updateUserStatus(id, dto.getActive());
        return ResponseEntity.ok().build();
    }
}
