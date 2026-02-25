package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Usando SP para obtener credenciales por email
    @Query(value = "SELECT * FROM sp_find_user_credentials_by_email(:email)", nativeQuery = true)
    Optional<UserCredentialsProjection> findCredentialsByEmail(@Param("email") String email);

    // Mapear resultado del SP
    interface UserCredentialsProjection {
        Integer getUserId();
        String getFirstName();
        String getLastName();
        String getEmail();
        Boolean getIsActive();
        String getUsername();
        String getPasswordHash();
    }
}
