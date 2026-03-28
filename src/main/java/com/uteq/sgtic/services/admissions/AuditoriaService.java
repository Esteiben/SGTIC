package com.uteq.sgtic.services.admissions;


import com.uteq.sgtic.repository.admissions.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    public List<Map<String, Object>> listarSesiones() {
        // La base de datos ya nos da todo masticado y formateado
        return auditoriaRepository.obtenerSesionesDesdeBD();
    }

    @Transactional
    public void expulsarUsuario(Long idUsuario) {
        Boolean exito = auditoriaRepository.forzarCierreSesionDB(idUsuario);

        if (Boolean.FALSE.equals(exito)) {
            throw new RuntimeException("No se pudo expulsar al usuario. El ID no existe.");
        }

        System.out.println("🚨 RAYO EXPULSOR ACTIVADO PARA EL USUARIO ID: " + idUsuario);
    }
}
