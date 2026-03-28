package com.uteq.sgtic.repository.InstitutionalStructurePackRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.uteq.sgtic.entities.Faculty;

@Repository

public interface FacultyCreateRepository extends JpaRepository <Faculty, Integer> {

    @Procedure (procedureName = "create_faculty") //Nombre directo del store procedure
    void createFaculty(@Param("p_name") String name, @Param("p_acronym") String acronym);
}
