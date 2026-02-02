package com.uteq.sgtic.controllers;

import com.uteq.sgtic.dtos.UserCreateSimpleDTO;
import com.uteq.sgtic.dtos.UserResponseDTO;
import com.uteq.sgtic.entities.User;
import com.uteq.sgtic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<UserResponseDTO> list = userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(toResponse(u)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<UserResponseDTO> create(@RequestBody UserCreateSimpleDTO dto) {
        User user = new User();
        user.setIdentification(dto.getIdentification());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPassword());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setActive(dto.getActive() != null ? dto.getActive() : true);

        User saved = userRepository.save(user);
        return ResponseEntity.status(201).body(toResponse(saved));
    }

    private UserResponseDTO toResponse(User u) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setIdUser(u.getIdUser());
        dto.setIdentification(u.getIdentification());
        dto.setUsername(u.getUsername());
        dto.setFirstName(u.getFirstName());
        dto.setLastName(u.getLastName());
        dto.setEmail(u.getEmail());
        dto.setActive(u.getActive());
        return dto;
    }
}
