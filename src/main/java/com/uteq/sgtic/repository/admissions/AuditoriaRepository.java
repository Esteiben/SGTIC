package com.uteq.sgtic.repository.admissions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AuditoriaRepository extends JpaRepository<com.uteq.sgtic.entities.User, Long> {

    // 1. Llama a la función que lista las sesiones
    @Query(value = "select * from public.fn_listar_sesiones_auditoria()", nativeQuery = true)
    List<Map<String, Object>> obtenerSesionesDesdeBD();

    // 2. Llama a la función del botón rojo
    @Query(value = "select public.fn_forzar_cierre_sesion(:id)", nativeQuery = true)
    Boolean forzarCierreSesionDB(@Param("id") Long idUsuario);
}
