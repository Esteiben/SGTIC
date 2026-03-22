package com.uteq.sgtic.services.teacher;

import com.uteq.sgtic.dtos.teacherDTO.TeacherDTO;
import com.uteq.sgtic.dtos.teacherDTO.TeacherSaveDTO;
import com.uteq.sgtic.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    public List<TeacherDTO> getDocentesPorCoordinador(Integer idUsuarioLogueado) {
        List<Object[]> resultados = teacherRepository.callListarDocentesPorCoordinador(idUsuarioLogueado);
        List<TeacherDTO> dtos = new ArrayList<>();

        for (Object[] fila : resultados) {
            TeacherDTO dto = new TeacherDTO();
            dto.setIdDocente((Integer) fila[0]);
            dto.setNombreCompleto((String) fila[1]);
            dto.setCorreo((String) fila[2]);
            String espStr = (String) fila[3];
            dto.setEspecialidades(espStr != null && !espStr.isEmpty() ? List.of(espStr.split(", ")) : new ArrayList<>());
            String estadoStr = (String) fila[4];
            dto.setEstado(estadoStr);
            dto.setActivo("activo".equalsIgnoreCase(estadoStr));
            dto.setCedula((String) fila[5]);
            dto.setNombres((String) fila[6]);
            dto.setApellidos((String) fila[7]);

            dtos.add(dto);
        }

        return dtos;
    }

    public void callActualizarEstado(Integer idDocente, String nuevoEstado) {
        teacherRepository.callActualizarEstado(idDocente, nuevoEstado);
    }

    public void guardarDocente(TeacherSaveDTO dto) {
        teacherRepository.callGestionDocente(
                dto.getIdDocente(),
                dto.getNombres(),
                dto.getApellidos(),
                dto.getCedula(),
                dto.getCorreo(),
                dto.getEspecialidades(),
                dto.getIdCarrera(),
                dto.getIdUsuarioLogueado()
        );
    }
}