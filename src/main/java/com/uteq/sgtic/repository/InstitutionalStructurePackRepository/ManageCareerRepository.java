package com.uteq.sgtic.repository.InstitutionalStructurePackRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uteq.sgtic.entities.Career;

@Repository
public interface ManageCareerRepository extends JpaRepository <Career, Integer> {
    @Procedure(procedureName = "manage_career")
    void manageCareer(
        @Param("p_id") Integer id,
        @Param("p_faculty") Integer faculty,
        @Param("p_name") String name,
        @Param("p_active") Boolean active
    );
}
