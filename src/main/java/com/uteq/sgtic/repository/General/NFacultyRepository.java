package com.uteq.sgtic.repository.General;

import com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO;
import com.uteq.sgtic.entities.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NFacultyRepository extends JpaRepository<Faculty, Integer> {
    
    // Instancia tu DTO directamente en la consulta JPQL
    @Query("SELECT new com.uteq.sgtic.dtos.requestAccess.SelectionItemDTO(f.idFaculty, f.name) FROM Faculty f WHERE f.active = true")
    List<SelectionItemDTO> findAllActiveFaculties();
}