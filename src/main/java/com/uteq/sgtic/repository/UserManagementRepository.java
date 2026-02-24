package com.uteq.sgtic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserManagementRepository extends JpaRepository<com.uteq.sgtic.entities.User, Integer> {

    @Query(value = "SELECT * FROM sp_create_user_complete(:ident, :nombres, :apellidos, :correo, :username, :passHash, :roles)",
            nativeQuery = true)
    CreationResult createUserComplete(
            @Param("ident") String identification,
            @Param("nombres") String firstName,
            @Param("apellidos") String lastName,
            @Param("correo") String email,
            @Param("username") String username,
            @Param("passHash") String passwordHash,
            @Param("roles") Integer[] roles  // Array de integers para PostgreSQL
    );

    // Obtener todos los usuarios con SP
    @Query(value = "SELECT * FROM sp_get_all_users()", nativeQuery = true)
    List<UserProjection> getAllUsersWithRoles();

    // Obtener roles disponibles
    @Query(value = "SELECT * FROM sp_get_available_roles()", nativeQuery = true)
    List<RoleProjection> getAvailableRoles();

    // Proyecciones para los resultados de SPs
    interface CreationResult {
        Integer getUserId();
        Boolean getSuccess();
        String getMessage();
    }

    interface UserProjection {
        Integer getIdUsuario();
        String getIdentificacion();
        String getNombres();
        String getApellidos();
        String getCorreo();
        Boolean getActivo();
        String getUsername();
        String[] getRoles();  // Array de PostgreSQL
    }

    interface RoleProjection {
        Integer getIdRol();
        String getNombre();
    }
}