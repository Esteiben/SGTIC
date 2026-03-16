package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Teacher;
import com.uteq.sgtic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    // Usando el correo del token JWT
    Optional<Teacher> findByUserEmail(String email);

    String user(User user);
}
