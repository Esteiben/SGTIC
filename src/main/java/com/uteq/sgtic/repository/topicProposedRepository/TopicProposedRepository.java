package com.uteq.sgtic.repository.topicProposedRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import com.uteq.sgtic.entities.StudentProposedTopic;
import java.util.List;

@Repository
public interface TopicProposedRepository extends JpaRepository<StudentProposedTopic, Integer> {

    @Query(value = "SELECT * FROM fn_listar_propuestas_pendientes(:idCoordinador)", nativeQuery = true)
    List<Object[]> listarPropuestasPendientes(@Param("idCoordinador") Integer idCoordinador);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_responder_propuesta(:idPropuesta, :nuevoEstado, :motivo)", nativeQuery = true)
    void responderPropuesta(
            @Param("idPropuesta") Integer idPropuesta,
            @Param("nuevoEstado") String nuevoEstado,
            @Param("motivo") String motivo
    );
}