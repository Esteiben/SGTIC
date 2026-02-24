package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(value = "SELECT * FROM sp_get_user_roles(:userId)", nativeQuery = true)
    List<String> findRoleNamesByUserId(@Param("userId") Integer userId);
}
