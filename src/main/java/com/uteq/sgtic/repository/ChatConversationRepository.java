package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {

    @Query("SELECT c FROM ChatConversation c WHERE c.idEstudiante = :idEstudiante AND c.idCoordinador = :idCoordinador")
    Optional<ChatConversation> findByEstudianteAndCoordinador(@Param("idEstudiante") Long idEstudiante, @Param("idCoordinador") Long idCoordinador);

    @Query("SELECT c FROM ChatConversation c WHERE c.idEstudiante = :idEstudiante")
    List<ChatConversation> findByEstudiante(@Param("idEstudiante") Long idEstudiante);

    @Query("SELECT c FROM ChatConversation c WHERE c.idCoordinador = :idCoordinador")
    List<ChatConversation> findByCoordinador(@Param("idCoordinador") Long idCoordinador);
}