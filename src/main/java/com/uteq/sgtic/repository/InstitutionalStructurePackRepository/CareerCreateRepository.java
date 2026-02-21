package com.uteq.sgtic.repository.InstitutionalStructurePackRepository;

import com.uteq.sgtic.entities.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerCreateRepository extends JpaRepository <Career, Integer> {
    @Procedure(procedureName = "create_career")
    void createCareer(@Param("p_faculty") Integer faculty, @Param("p_name") String name);
}
    