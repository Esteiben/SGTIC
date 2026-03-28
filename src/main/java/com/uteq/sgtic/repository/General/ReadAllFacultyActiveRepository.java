package com.uteq.sgtic.repository.General;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.Faculty;

@Repository

public interface ReadAllFacultyActiveRepository extends JpaRepository <Faculty, Integer> {
    List<Faculty> findByActiveTrue();
}