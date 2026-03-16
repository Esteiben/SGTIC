package com.uteq.sgtic.services.impl.student;

import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicDTO;
import com.uteq.sgtic.dtos.student.RegisterProposalStudentTopicResponseDTO;
import com.uteq.sgtic.repository.student.RegisterProposalStudentTopicRepository;
import com.uteq.sgtic.services.AzureStorageConfig;
import com.uteq.sgtic.services.student.IRegisterProposalStudentTopicServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RegisterProposalStudentTopicServicesImpl implements IRegisterProposalStudentTopicServices {

    private final RegisterProposalStudentTopicRepository registerProposalStudentTopicRepository;
    private final AzureStorageConfig azureStorageConfig;

    @Override
    public RegisterProposalStudentTopicResponseDTO registrarPropuestaTema(RegisterProposalStudentTopicDTO dto) throws Exception {

        String urlDocumento = null;
        MultipartFile archivo = dto.getDocumento();

        if (archivo != null && !archivo.isEmpty()) {
            validarArchivo(archivo);
            urlDocumento = azureStorageConfig.subirDocumento(archivo);
        }

        Integer idPropuesta = registerProposalStudentTopicRepository.registrarPropuestaTemaEstudiante(
                dto.getIdUsuario(),
                dto.getIdOpcion(),
                dto.getTitulo(),
                dto.getDescripcion(),
                urlDocumento
        );

        return RegisterProposalStudentTopicResponseDTO.builder()
                .idPropuesta(idPropuesta)
                .mensaje("Propuesta registrada correctamente")
                .urlDocumento(urlDocumento)
                .build();
    }

    private void validarArchivo(MultipartFile archivo) {
        String nombre = archivo.getOriginalFilename();

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El archivo no tiene un nombre válido");
        }

        String nombreLower = nombre.toLowerCase();

        boolean extensionValida =
                nombreLower.endsWith(".pdf") ||
                nombreLower.endsWith(".doc") ||
                nombreLower.endsWith(".docx");

        if (!extensionValida) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, DOC o DOCX");
        }

        long maxBytes = 10 * 1024 * 1024; // 10 MB
        if (archivo.getSize() > maxBytes) {
            throw new IllegalArgumentException("El archivo supera el tamaño máximo de 10 MB");
        }
    }
}