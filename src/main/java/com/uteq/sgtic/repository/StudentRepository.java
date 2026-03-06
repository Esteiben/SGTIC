package com.uteq.sgtic.repository;

import com.uteq.sgtic.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUserId(Integer userId);
    Optional<Student> findByUserIdentification(String identification);
}
