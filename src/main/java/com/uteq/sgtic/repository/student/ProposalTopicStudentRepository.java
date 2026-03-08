package com.uteq.sgtic.repository.student;

import com.uteq.sgtic.entities.StudentProposedTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProposalTopicStudentRepository extends JpaRepository<StudentProposedTopic, Integer> {

    @Query(value = "SELECT fn_insertar_propuesta_tema(:idEstudiante, :idOpcion, :titulo, :descripcion, :archivoUrl)", nativeQuery = true)
    Integer insertarPropuesta(
        @Param("idEstudiante") Integer idEstudiante,
        @Param("idOpcion") Integer idOpcion,
        @Param("titulo") String titulo,
        @Param("descripcion") String descripcion,
        @Param("archivoUrl") String archivoUrl
    );
}