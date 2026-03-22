package com.uteq.sgtic.repository.General;

import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import com.uteq.sgtic.entities.Career;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NCareerRepository extends JpaRepository<Career, Integer> {
    
    // Filtra las carreras por el ID de la facultad
    @Query("SELECT new com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO(c.idCareer, c.name) FROM Career c WHERE c.faculty.idFaculty = :facultyId AND c.active = true")
    List<SelectionItemDTO> findCareersByFacultyId(@Param("facultyId") Integer facultyId);
}