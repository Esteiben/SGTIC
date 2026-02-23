package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("select t from Teacher t join t.user u " +
            "where lower(u.firstName) like lower(concat('%', :criteria, '%')) " +
            "or lower(u.lastName) like lower(concat('%', :criteria, '%')) " +
            "or lower(u.email) like lower(concat('%', :criteria, '%'))")
    List<Teacher> findTeachersByCriteria(@Param("criteria") String criteria);
}