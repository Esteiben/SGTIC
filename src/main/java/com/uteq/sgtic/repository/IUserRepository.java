package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {
    @Procedure(procedureName = "sp_crear_usuario")
    void createUser(
            String p_identificacion,
            String p_username,
            String p_password_hash,
            String p_nombres,
            String p_apellidos,
            String p_correo
    );
    Optional<User> findByEmail(String email);
}
