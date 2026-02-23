package com.uteq.sgtic.services;

import com.uteq.sgtic.dtos.TeacherResponseDTO;
import com.uteq.sgtic.entities.Teacher;
import com.uteq.sgtic.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherResponseDTO> searchTeachers(String criteria) {
        List<Teacher> teachers;

        if (criteria == null || criteria.trim().isEmpty()) {
            teachers = teacherRepository.findAll();
        } else {
            teachers = teacherRepository.findTeachersByCriteria(criteria);
        }

        return teachers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    private TeacherResponseDTO convertToDTO(Teacher teacher) {
        TeacherResponseDTO dto = new TeacherResponseDTO();

        dto.setIdTeacher(teacher.getIdTeacher());
        String fullName = teacher.getUser().getFirstName() + " " + teacher.getUser().getLastName();
        dto.setFullName(fullName);

        dto.setEmail(teacher.getUser().getEmail());
        dto.setDegree(teacher.getDegree());
        dto.setIsResearcher(teacher.getIsResearcher());

        dto.setContract("Tiempo Completo");
        if (teacher.getIdTeacher() % 2 == 0) {
            dto.setWorkload(5);
        } else {
            dto.setWorkload(3);
        }

        return dto;
    }
}