package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    // Buscar mensajes por conversación
    @Query("SELECT m FROM ChatMessageEntity m WHERE m.idConversacion = :idConversacion ORDER BY m.fechaEnvio ASC")
    List<ChatMessageEntity> findByConversacion(@Param("idConversacion") Long idConversacion);

    // Contar mensajes no leídos
    @Query("SELECT COUNT(m) FROM ChatMessageEntity m WHERE m.idConversacion = :idConversacion AND m.estado = 'enviado' AND m.idRemitente != :idUsuario")
    int countNoLeidos(@Param("idConversacion") Long idConversacion, @Param("idUsuario") Long idUsuario);

    //  OBTENER TODOS LOS MENSAJES ORDENADOS POR FECHA (ESTE FUNCIONA)
    List<ChatMessageEntity> findAllByOrderByFechaEnvioAsc();

    //  MeTODO CORREGIDO PARA BUSCAR POR CORREO (USANDO CONSULTA NATIVA)
    @Query(value = "SELECT cm.* FROM chat_messages cm " +
            "JOIN usuario u ON cm.id_remitente = u.id_usuario " +
            "WHERE u.correo = :correo " +
            "ORDER BY cm.fecha_envio ASC", nativeQuery = true)
    List<ChatMessageEntity> findByUsuarioCorreo(@Param("correo") String correo);

    // Usamos una Query manual para evitar errores de nombres de propiedad
    @Query("SELECT m FROM ChatMessageEntity m WHERE m.idConversacion = :idConversacion ORDER BY m.fechaEnvio ASC")
    List<ChatMessageEntity> findByConversacionPersonalizada(@Param("idConversacion") Long idConversacion);
}