package com.uteq.sgtic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uteq.sgtic.entities.User;

@Repository
public interface UserContextRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT * FROM sp_get_user_context(:userId)", nativeQuery = true)
    UserContextProjection findUserContext(@Param("userId") Integer userId);

    interface UserContextProjection {
        Integer getIdFaculty();
        Integer getIdCareer();
        Integer getIdTeacher();
        Integer getIdStudent();
    }
}
