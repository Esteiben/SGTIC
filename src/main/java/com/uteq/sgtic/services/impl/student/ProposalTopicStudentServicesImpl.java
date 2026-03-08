package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.ProposalTopicStudentDTO;
import com.uteq.sgtic.repository.student.ProposalTopicStudentRepository;
import com.uteq.sgtic.services.student.IProposalTopicStudent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProposalTopicStudentServicesImpl implements IProposalTopicStudent {

    private final ProposalTopicStudentRepository repository;
    private final String uploadDir = "uploads/propuestas/";

    @Override
    public void crearPropuesta(Integer idEstudiante, ProposalTopicStudentDTO dto, MultipartFile archivo) {
        String archivoUrl = null;

        if (archivo != null && !archivo.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                archivo.transferTo(filePath.toFile());
                archivoUrl = "/uploads/propuestas/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("Error al guardar el archivo", e);
            }
        }
    }
}