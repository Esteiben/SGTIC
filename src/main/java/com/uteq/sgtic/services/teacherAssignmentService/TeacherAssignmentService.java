package com.uteq.sgtic.services.teacherAssignmentService;

import com.uteq.sgtic.dtos.teacherAssignmentDTO.PendingProjectDTO;
import com.uteq.sgtic.dtos.teacherAssignmentDTO.TeacherAssignmentDTO;
import com.uteq.sgtic.repository.teacherAssignmentRepository.TeacherAssignmentRepository;
import com.uteq.sgtic.repository.AcademicPeriodRepository;
import com.uteq.sgtic.entities.AcademicPeriod;
import com.uteq.sgtic.services.admissions.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherAssignmentService {

    @Autowired
    private TeacherAssignmentRepository docenteRepository;

    @Autowired
    private AcademicPeriodRepository periodRepository;

    @Autowired
    private EmailService emailService;

    public List<TeacherAssignmentDTO> listarDocentesParaAsignacion(Integer idUsuarioCoordinador) {
        List<Object[]> resultados = docenteRepository.findDocentesByCoordinador(idUsuarioCoordinador);

        return resultados.stream().map(row -> {
            String[] especialidadesArray = (String[]) row[2];
            List<String> especialidadesList = (especialidadesArray != null)
                    ? Arrays.asList(especialidadesArray)
                    : Collections.emptyList();

            return new TeacherAssignmentDTO(
                    (Integer) row[0],
                    (String) row[1],
                    especialidadesList,
                    ((Number) row[3]).intValue(),
                    0.0,
                    null
            );
        }).collect(Collectors.toList());
    }

    public List<PendingProjectDTO> listarPropuestasPendientes(Integer idCoordinador) {
        List<Object[]> results = docenteRepository.findProyectosPendientes(idCoordinador);

        return results.stream().map(row -> PendingProjectDTO.builder()
                .idPropuesta((Integer) row[0])
                .titulo((String) row[1])
                .nombreEstudiante((String) row[2])
                .descripcion((String) row[3])
                .fechaEnvio((String) row[4])
                .build()
        ).collect(Collectors.toList());
    }

    @Transactional
    public void ejecutarAsignacion(Integer idPropuesta, Integer idDocente, Integer idPeriodoEnviado) {
        Integer idPeriodoFinal = idPeriodoEnviado;

        if (idPeriodoFinal == null || idPeriodoFinal == 0) {
            idPeriodoFinal = periodRepository.findAll().stream()
                    .filter(AcademicPeriod::getActive)
                    .map(AcademicPeriod::getIdPeriod)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Error: No existe un periodo académico activo."));
        }

        docenteRepository.asignarDirector(idPropuesta, idDocente, idPeriodoFinal);

        try {
            List<Object[]> datosParaCorreo = docenteRepository.findDatosParaNotificacion(idPropuesta, idDocente);

            if (datosParaCorreo != null && !datosParaCorreo.isEmpty()) {
                Object[] row = datosParaCorreo.get(0);
                String correoDocente = (String) row[0];
                String nombreDocente = (String) row[1];
                String tituloProyecto = (String) row[2];
                String nombreEstudiante = (String) row[3];

                emailService.enviarNotificacionAsignacionDirector(
                        correoDocente,
                        nombreDocente,
                        tituloProyecto,
                        nombreEstudiante
                );
            }
        } catch (Exception e) {
            System.err.println("Asignación guardada en BD, pero falló el envío de correo: " + e.getMessage());
        }
    }
}