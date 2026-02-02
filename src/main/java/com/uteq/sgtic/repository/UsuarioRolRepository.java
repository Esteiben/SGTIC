package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.UserRole;
import com.uteq.sgtic.entities.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UserRole, UserRoleId> {
}
